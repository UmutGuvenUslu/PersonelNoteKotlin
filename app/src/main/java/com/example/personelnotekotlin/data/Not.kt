package com.example.personelnotekotlin.data

import java.util.UUID

data class Not(
    val _id:String = UUID.randomUUID().toString(),
    val baslik:String,
    val aciklama:String,
    val oncelik:Int,
    val guncellemeTarihi:Long = System.currentTimeMillis(),
    val senkronMu:Boolean = false,
    val silindiMi:Boolean = false,
    val sunucudaVarMi:Boolean = false

)