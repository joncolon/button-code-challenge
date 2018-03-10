package colon.jonathan.buttoncodechallenge.data.cache

import DEBUG
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cache @Inject constructor() {

    private val cache: MutableMap<String, Any> = Collections.synchronizedMap(HashMap())
    private val expirationTime: MutableMap<String, Long> = Collections.synchronizedMap(HashMap())

    private val CACHE_LIFE_MILLISECONDS: Long = 15 * 1000

    fun updateCache(url: String, result: Any) {
        synchronized(this) {
            cache.put(url, result)
            expirationTime.put(url, System.currentTimeMillis() + CACHE_LIFE_MILLISECONDS)
        }
    }

    fun isCached(url: String): Boolean {
        synchronized(this) {
            return cache.contains(url)
        }
    }

    fun isValid(url: String): Boolean {
        synchronized(this) {
            return expirationTime.contains(url) && (expirationTime[url]!! > System.currentTimeMillis())
        }
    }

    fun getCached(url: String): Any? {
        return cache[url]
    }

    fun clearCache() {
        cache.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onCreate() {
        DEBUG("Starting Cache")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        DEBUG("Shutting down Cache")
    }
}