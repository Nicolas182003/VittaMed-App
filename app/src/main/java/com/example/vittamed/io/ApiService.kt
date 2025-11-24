package com.example.vittamed.io
import com.example.vittamed.io.response.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST(value = "login")
    fun postLogin(@Query(value = "email") email: String, @Query(value = "password") password: String):
            Call<LoginResponse>

    @POST( value = "logout")
    fun postLogout(@Header(value = "Authorization") authHeader: String):
            Call<Void>

    companion object Factory{
        private const val BASE_URL = "http://192.168.100.42:8000/api/"
        fun create(): ApiService{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}