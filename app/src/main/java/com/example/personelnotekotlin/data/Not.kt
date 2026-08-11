package com.example.personelnotekotlin.data

import java.util.UUID

data class Not(
    var _id:String = UUID.randomUUID().toString(),
    var baslik:String,
    var aciklama:String,
    var oncelik:Int,
    var guncellemeTarihi:Long = System.currentTimeMillis(),
    var senkronMu:Boolean = false,
    var silindiMi:Boolean = false,
    var sunucudaVarMi:Boolean = false

)