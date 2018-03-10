package colon.jonathan.buttoncodechallenge.data

import DEBUG
import ERROR
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.OnLifecycleEvent
import colon.jonathan.buttoncodechallenge.data.cache.Cache
import colon.jonathan.buttoncodechallenge.data.livedata.LoadingState.LOADING
import colon.jonathan.buttoncodechallenge.data.livedata.LoadingState.NOT_LOADING
import colon.jonathan.buttoncodechallenge.data.livedata.LoadingStateLiveData
import colon.jonathan.buttoncodechallenge.data.livedata.NetworkError.*
import colon.jonathan.buttoncodechallenge.data.livedata.NetworkErrorLiveData
import colon.jonathan.buttoncodechallenge.data.local.DatabaseHelper
import colon.jonathan.buttoncodechallenge.data.remote.ButtonHttpClient
import colon.jonathan.buttoncodechallenge.data.remote.HttpConstants
import colon.jonathan.buttoncodechallenge.model.User
import colon.jonathan.buttoncodechallenge.utilities.NetworkConnectivityHelper
import colon.jonathan.buttoncodechallenge.utilities.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import okhttp3.ResponseBody
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @description: Repository is the point of access both remote and local data. Includes
 * cache support, network connectivity awareness, Room database access, and uses RxJava 2 for
 * handling HTTP Requests.
 */
@Singleton
class Repository @Inject constructor(
        val networkConnectivity: NetworkConnectivityHelper,
        val cache: Cache,
        val database: DatabaseHelper,
        val client: ButtonHttpClient,
        val schedulerProvider: SchedulerProvider,
        val liveNetworkErrorData: NetworkErrorLiveData,
        val liveLoadingStateData: LoadingStateLiveData) {

    var compositeDisposable = CompositeDisposable()

    private fun checkCache(): Boolean {
        val cacheKey = HttpConstants.USER_PATH
        val isCached = cache.isCached(cacheKey)
        val isValid = cache.isValid(cacheKey)
        if (isCached && (isValid || networkConnectivity.isConnected())) {
            return true
        }

        if (!isCached && networkConnectivity.isOffline()) {
            return true
        }
        return false
    }

    fun postUser(user: User): Disposable? {
        return client.postUser(user)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableSingleObserver<ResponseBody>() {
                    override fun onSuccess(response: ResponseBody) {
                        DEBUG("POST DOWNLOAD_SUCCESS: $response")
                        liveNetworkErrorData.setNetworkError(POST_SUCCESS)
                        downloadUsers()
                    }

                    override fun onError(e: Throwable) {
                        ERROR("POST ERROR: ${e.message}")
                        liveNetworkErrorData.setNetworkError(POST_FAILURE)
                    }
                })
    }


    fun getUsers(): LiveData<List<User>> {
        compositeDisposable.add(downloadUsers())
        return database.getAllUsers()
    }

    private fun downloadUsers(): Disposable {
        liveLoadingStateData.setLoadingState(LOADING)

        return client.downloadUsers(HttpConstants.CANDIDATE).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        { users: List<User> ->
                            liveNetworkErrorData.setNetworkError(DOWNLOAD_SUCCESS)
                            liveLoadingStateData.setLoadingState(NOT_LOADING)

                            DEBUG("${users.size} retrieved from INTERNET")
                            DEBUG("USERS : $users")
                            DEBUG("Attempting to update database")
                            DEBUG("Inserting ${users.size} into database")

                            cache.updateCache(HttpConstants.USER_PATH, users)
                            users.forEach {
                                database.insertUser(it.mutable())
                            }
                        },
                        { error ->
                            liveLoadingStateData.setLoadingState(NOT_LOADING)
                            liveNetworkErrorData.setNetworkError(DOWNLOAD_FAILURE)
                            when (error) {
                                is UnknownHostException -> {
                                    liveNetworkErrorData.setNetworkError(NO_CONNECTION)
                                }
                                else -> {
                                    liveNetworkErrorData.setNetworkError(DOWNLOAD_FAILURE)
                                }
                            }
                            ERROR("Failed to download : $error")
                        })
    }

    fun getUser(userId: Int): LiveData<User> {
        return database.getUserById(userId)
    }

    fun clearCache() {
        cache.clearCache()
        DEBUG("cache cleared: ${cache.isCached(HttpConstants.USER_PATH)}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun clearDisposables() {
        compositeDisposable.dispose()
        DEBUG("LifeCycleEvent OnStop: called")
        DEBUG("Disposables cleared: ${compositeDisposable.isDisposed}")
    }
}
