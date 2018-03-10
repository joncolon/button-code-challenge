package colon.jonathan.buttoncodechallenge.data.remote

import colon.jonathan.buttoncodechallenge.model.User
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*


interface ButtonHttpClient {

    @POST(HttpConstants.USER_PATH)
    fun postUser(
            @Body user: User
    ): Single<ResponseBody>

    @GET(HttpConstants.USER_PATH)
    fun downloadUserById(
            @Query(":id") id: Int,
            @Query("candidate") candidate: String
    ): Single<User>

    @GET(HttpConstants.USER_PATH)
    fun downloadUsers(
            @Query("candidate") candidate: String
    ): Single<List<User>>
}