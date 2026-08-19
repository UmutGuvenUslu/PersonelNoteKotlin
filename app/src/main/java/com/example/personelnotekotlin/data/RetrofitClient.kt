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

            if (OturumYoneticisi.tokenGetir() != ""){
                newRequest.header("Authorization","Bearer ${OturumYoneticisi.tokenGetir()}")
            }

            chain.proceed(newRequest.build())
        }
        .build()

    val apiService: Retrofit = Retrofit.Builder()
        .baseUrl("https://b2e1-2a00-1d34-d4a0-5300-d10-b661-a55e-e3c5.ngrok-free.app/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val notApiService: INotApi = apiService.create(INotApi::class.java)
    val kategoriApiService: IKategoriApi = apiService.create(IKategoriApi::class.java)
    val authApiService: IAuthApi = apiService.create(IAuthApi::class.java)
    val kullaniciApiService: IKullaniciApi = apiService.create(IKullaniciApi::class.java)

}
