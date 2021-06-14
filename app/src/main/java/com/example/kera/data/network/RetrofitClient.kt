package com.example.kera.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient(val token: String?) {
    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var client =
        OkHttpClient.Builder().readTimeout(1000, TimeUnit.SECONDS).callTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization",getToken(token) )
                .build()
            chain.proceed(newRequest)
        }.addInterceptor(logging).build()

    fun getService(): ApiService {
        return Retrofit.Builder().client(client)
            .baseUrl("https://kera-app.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(ApiService::class.java)
    }
    fun getToken(token: String?) : String{
        if (token == "kera-app"){
            return "kera-app"
        }else{
            return "Bearer $token"
        }

    }
}