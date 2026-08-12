package com.example.personelnotekotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Not(
    @PrimaryKey
    var _id:String = UUID.randomUUID().toString(),
    var baslik:String,
    var aciklama:String,
    var oncelik:Int,
    var guncellemeTarihi:Long = System.currentTimeMillis(),
    var senkronMu:Boolean = false,
    var silindiMi:Boolean = false,
    var sunucudaVarMi:Boolean = false

)