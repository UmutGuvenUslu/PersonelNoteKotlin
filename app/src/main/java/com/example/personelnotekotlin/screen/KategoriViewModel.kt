package com.example.personelnotekotlin.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personelnotekotlin.data.Kategori
import com.example.personelnotekotlin.data.KategoriRepository
import com.example.personelnotekotlin.data.Not
import com.example.personelnotekotlin.data.NotRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KategoriViewModel(
    private var repository: KategoriRepository
):ViewModel()
{
    init {
        kategoriSenkronizeEt()
    }

    var kategorilistesi:StateFlow<List<Kategori>> = repository.aktifKategorileriGetir()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun kategoriEkle(isim:String){
        viewModelScope.launch {
            repository.kategoriEkle(isim)
        }
    }

    fun kategoriDuzenle(kategori: Kategori,isim:String){
        viewModelScope.launch {
            repository.kategoriDuzenle(kategori,isim)
        }
    }

    fun kategoriSil(kategori: Kategori) {
        viewModelScope.launch {
            repository.kategoriSil(kategori)
        }
    }

    fun kategoriSenkronizeEt(){
        viewModelScope.launch {
            repository.tamSenkronizasyonYap()
        }
    }

}