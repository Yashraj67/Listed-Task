package com.example.todo.api


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val BASE_URL = "https://api.inopenapp.com/"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            // Add Bearer token
            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
            if (token.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            // Add Cookie
            val cookie = "connect.sid=s%3AUexWNZh16q8vdcCqi9apxst5v9WFuKx8.SN8zJLvi2IE8WtzJReOCB8SJqHbSivfXBd19mSoPz38"
            if (cookie.isNotEmpty()) {
                requestBuilder.addHeader("Cookie", cookie)
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    fun getInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}