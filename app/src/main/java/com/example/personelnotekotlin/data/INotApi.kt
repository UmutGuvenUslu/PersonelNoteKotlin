package com.example.personelnotekotlin.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface INotApi {

    @GET("notes")
    suspend fun notGetir(@Query("sayfa") sayfa: Int? = null):Response<List<Not>>

    @POST("note")
    suspend fun notEkle(@Body not: Not):Response<Not>

    @DELETE("note/{id}")
    suspend fun notSil(@Path("id") id:String):Response<Unit>

    @PUT("note/{id}")
    suspend fun notDuzenle(@Path("id") id:String,@Body not: Not):Response<Not>

    @POST("notes/sync")
    suspend fun notSenkronizeEt(@Body notlar:List<Not>):Response<Any>


}