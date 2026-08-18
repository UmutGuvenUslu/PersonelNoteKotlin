package com.example.personelnotekotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity
data class Not(
    @PrimaryKey
    @SerializedName("_id")
    var _id: String = UUID.randomUUID().toString(),

    @SerializedName("baslik")
    var baslik: String,

    @SerializedName("aciklama")
    var aciklama: String,

    @SerializedName("kullaniciId")
    var kullaniciId: String,

    @SerializedName("kategoriId")
    var kategoriId: String,

    @SerializedName("oncelik")
    var oncelik: Int,

    @SerializedName("guncellemeTarihi")
    var guncellemeTarihi: Long = System.currentTimeMillis(),

    @SerializedName("senkronMu")
    var senkronMu: Boolean = false,

    @SerializedName("silindiMi")
    var silindiMi: Boolean = false,

    @SerializedName("sunucudaVarMi")
    var sunucudaVarMi: Boolean = false
)