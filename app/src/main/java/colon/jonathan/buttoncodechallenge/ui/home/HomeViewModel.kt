package colon.jonathan.buttoncodechallenge.ui.home

import DEBUG
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import colon.jonathan.buttoncodechallenge.data.Repository
import colon.jonathan.buttoncodechallenge.data.livedata.LoadingState
import colon.jonathan.buttoncodechallenge.data.livedata.NetworkError
import colon.jonathan.buttoncodechallenge.model.User
import colon.jonathan.buttoncodechallenge.data.remote.HttpConstants
import javax.inject.Inject


class HomeViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private var liveUserData: LiveData<List<User>> = MutableLiveData<List<User>>()
    private var liveNetworkErrorData: LiveData<NetworkError> = MutableLiveData<NetworkError>()
    private var liveLoadingStateData: LiveData<LoadingState> = MutableLiveData<LoadingState>()

    init {
        liveNetworkErrorData = repository.liveNetworkErrorData
        liveLoadingStateData = repository.liveLoadingStateData
    }

    fun getLiveUserList(): LiveData<List<User>> {
        liveUserData = repository.getUsers()
        return liveUserData
    }

    fun getLiveLoadingState(): LiveData<LoadingState> {
        return liveLoadingStateData
    }

    fun getLiveNetworkData(): LiveData<NetworkError> {
        return liveNetworkErrorData
    }

    fun createUser(name: String, email: String) {
        val user = User(-1, name, email, HttpConstants.CANDIDATE)
        repository.postUser(user)
    }

    fun refreshData() {
        liveUserData = repository.getUsers()
    }

    override fun onCleared() {
        super.onCleared()
        DEBUG("ON CLEARED CALLED")
    }
}