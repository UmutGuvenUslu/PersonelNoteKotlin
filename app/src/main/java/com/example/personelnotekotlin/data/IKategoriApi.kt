package com.example.personelnotekotlin.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IKategoriApi {

    @GET("kategori")
    suspend fun kategorileriGetir():Response<List<Kategori>>

    @POST("kategori/sync")
    suspend fun kategoriSenkronizeEt(@Body kategoriler: List<Kategori>):Response<Any>
}