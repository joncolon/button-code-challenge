package colon.jonathan.buttoncodechallenge.injection

import colon.jonathan.buttoncodechallenge.data.remote.ButtonHttpClient
import colon.jonathan.buttoncodechallenge.data.remote.HttpConstants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val httpClient = OkHttpClient.Builder()

        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)  // <-- this is the important line!
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideOkcClient(client: OkHttpClient): ButtonHttpClient {
        return Retrofit.Builder()
                .baseUrl(HttpConstants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(ButtonHttpClient::class.java)
    }
}
