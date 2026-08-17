package com.example.personelnotekotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity
data class Kategori (

    @PrimaryKey
    var _id:String = UUID.randomUUID().toString(),
    var isim:String,
    var senkronMu:Boolean = false,
    var silindiMi:Boolean = false,
    var sunucudaVarMi:Boolean = false,
    var guncellemeTarihi:Long = System.currentTimeMillis()

    )