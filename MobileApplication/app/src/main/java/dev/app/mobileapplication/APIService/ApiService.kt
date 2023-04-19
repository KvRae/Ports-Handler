package dev.app.mobileapplication

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    //*********************** Sign in ***********************//
    @Headers("Content-Type:application/json")
    @POST("users/login")
    fun loginUser(@Body info: User): Call<ResponseBody>

    //***************** Get Ports by user *******************//
    @Headers("Content-Type:application/json")
    @GET("ports/user/{id}")
    fun getPortsbUser(@Path("id") carte_user: String): Call<List<Port>>

}

data class User(
    val nom: String,
    val password: String,
    val num_carte: String
)

data class Port(
    val id: String,
    val nom: String,
    val carte_user: String
)

class RetrofitInstance {
    companion object {

        const val BASE_URL: String = "http://10.0.2.2:5000/"
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}