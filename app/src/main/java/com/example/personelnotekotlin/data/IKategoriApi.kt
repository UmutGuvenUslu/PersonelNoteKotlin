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

    @POST("kategoriEkle")
    suspend fun kategoriEkle(@Body kategori: Kategori):Response<Kategori>

    @PUT("kategoriDuzenle/{id}")
    suspend fun kategoriDuzenle(@Path("id") id:String ,@Body kategori: Kategori):Response<Kategori>

    @DELETE("kategori/{id}")
    suspend fun kategoriSil(@Path("id") id:String):Response<Unit>

    @POST("kategori/sync")
    suspend fun kategoriSenkronizeEt(@Body kategoriler: List<Kategori>):Response<Any>
}