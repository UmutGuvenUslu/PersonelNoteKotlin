package com.example.personelnotekotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Kullanici(
    @PrimaryKey
    var _id:String = UUID.randomUUID().toString(),
    var isim:String,
    var soyisim:String,
    var sifre:String,
    var notlar:List<String> = emptyList(),
    var sunucudaVarMi:Boolean = false,
    var silindiMi:Boolean = false,
    var senkronMu:Boolean = false,
    var guncellemeTarihi:Long = System.currentTimeMillis()

    )