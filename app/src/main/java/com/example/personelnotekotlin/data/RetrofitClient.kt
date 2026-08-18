package com.example.personelnotekotlin.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("ngrok-skip-browser-warning", "true")
                .build()
            chain.proceed(newRequest)
        }
        .build()

    val apiService: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:1880/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val notApiService: INotApi = apiService.create(INotApi::class.java)
    val kategoriApiService: IKategoriApi = apiService.create(IKategoriApi::class.java)

}