package com.app.kera.data.network

import android.content.Context
import com.app.kera.BuildConfig
import com.app.kera.app.NoConnectionInterceptor
import com.app.kera.utils.Configurations.Companion.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient(val token: String?,var lang:String,val context: Context) {
    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var client =
        OkHttpClient.Builder() .addInterceptor(NoConnectionInterceptor(context)).readTimeout(1000, TimeUnit.SECONDS).callTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization",getToken(token) )
                .addHeader("lang",lang)
                .build()
            chain.proceed(newRequest)
        }.addInterceptor(logging).build()

    fun getService(): ApiService {
        return Retrofit.Builder().client(client)
            .baseUrl(BuildConfig.BASE_URL)
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