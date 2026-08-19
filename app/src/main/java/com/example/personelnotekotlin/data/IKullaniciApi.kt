package com.example.personelnotekotlin.data

import retrofit2.Response
import retrofit2.http.GET

interface IKullaniciApi {

    @GET("user")
    suspend fun kullanicilariGetir():Response<List<Kullanici>>
}
