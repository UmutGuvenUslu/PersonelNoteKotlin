package com.example.personelnotekotlin.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personelnotekotlin.data.Not
import com.example.personelnotekotlin.data.NotRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotViewModel(
    private var repository: NotRepository
):ViewModel()
{
    init {
        notSenkronizeEt()
    }

    var notListesi:StateFlow<List<Not>> = repository.aktifNotlariGetir()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun notEkle(baslik:String,aciklama:String,oncelik:String,kategoriId:String,kullaniciId:String){
        viewModelScope.launch {
            repository.notEkle(baslik,aciklama, metinToSayi(oncelik),kategoriId, kullaniciId)
        }
    }

    fun notDuzenle(not: Not,baslik:String,aciklama:String,oncelik:String,kategoriId:String,kullaniciId:String){
        viewModelScope.launch {
            repository.notDuzenle(not,baslik,aciklama,metinToSayi(oncelik),kategoriId, kullaniciId)
        }
    }

    fun notSil(not: Not) {
        viewModelScope.launch {
            repository.notSil(not)
        }
    }

    fun notSenkronizeEt(){
        viewModelScope.launch {
            repository.tamSenkronizasyonYap()
        }
    }

    private fun metinToSayi(deger:String):Int{
        var sayi = 1
        if (deger == "Kritik"){
            sayi = 3
        }else if(deger == "Orta"){
            sayi = 2
        }
        return sayi
    }

    fun sayiToMetin(deger:Int):String{
        var metin = "Düşük"
        if (deger == 3){
            metin = "Kritik"
        }else if(deger == 2){
            metin = "Orta"
        }
        return metin
    }



}