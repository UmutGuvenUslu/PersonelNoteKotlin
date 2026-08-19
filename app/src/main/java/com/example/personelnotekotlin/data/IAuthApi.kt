package com.example.personelnotekotlin.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class GirisIstek(
    var kullaniciAdi:String,
    var sifre:String
)

data class GirisYanit(
    var token:String,
    var kullanici: Kullanici
)

interface IAuthApi {

    @POST("login")
    suspend fun girisYap(@Body istek: GirisIstek):Response<GirisYanit>
}
