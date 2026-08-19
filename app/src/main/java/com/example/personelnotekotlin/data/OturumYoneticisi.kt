package com.example.personelnotekotlin.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

val Context.oturumStore by preferencesDataStore(name = "oturum")

private val TOKEN = stringPreferencesKey("token")
private val KULLANICIID = stringPreferencesKey("kullaniciId")
private val ROL = stringPreferencesKey("rol")
private val ADSOYAD = stringPreferencesKey("adSoyad")

object OturumYoneticisi {

    var tokenAkisi = MutableStateFlow("")
    var kullaniciId = ""
    var rol = ""
    var adSoyad = ""

    fun tokenGetir():String{
        return tokenAkisi.value
    }

    fun adminMi():Boolean{
        return rol == "admin"
    }

    suspend fun oturumYukle(context: Context){
        var veriler = context.oturumStore.data.first()

        kullaniciId = veriler[KULLANICIID] ?: ""
        rol = veriler[ROL] ?: ""
        adSoyad = veriler[ADSOYAD] ?: ""
        tokenAkisi.value = veriler[TOKEN] ?: ""
    }

    suspend fun oturumKaydet(context: Context,token:String,id:String,kullaniciRolu:String,isimSoyisim:String){
        context.oturumStore.edit {
            it[TOKEN] = token
            it[KULLANICIID] = id
            it[ROL] = kullaniciRolu
            it[ADSOYAD] = isimSoyisim
        }

        kullaniciId = id
        rol = kullaniciRolu
        adSoyad = isimSoyisim
        tokenAkisi.value = token
    }

    suspend fun oturumTemizle(context: Context){
        context.oturumStore.edit {
            it.clear()
        }

        kullaniciId = ""
        rol = ""
        adSoyad = ""
        tokenAkisi.value = ""
    }
}
