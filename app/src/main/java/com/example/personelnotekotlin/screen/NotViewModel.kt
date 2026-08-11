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
    var notListesi:StateFlow<List<Not>> = repository.aktifNotlariGetir()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun notEkle(baslik:String,aciklama:String,oncelik:String){
        viewModelScope.launch {
            repository.notEkle(baslik,aciklama,metinToSayi(oncelik))
        }
    }

    fun notSil(not: Not) {
        viewModelScope.launch {
            repository.notSil(not)
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



}