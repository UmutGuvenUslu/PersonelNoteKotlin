package com.example.personelnotekotlin.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personelnotekotlin.data.Not
import com.example.personelnotekotlin.data.NotRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.ceil

data class NotlarSayfaState(
    val gosterilenNotlar: List<Not> = emptyList(),
    val mevcutSayfa: Int = 1,
    val sonrakiVarMi: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class NotViewModel(
    private var repository: NotRepository
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    init {
        notSenkronizeEt()
    }

    private val secilenKategoriId = MutableStateFlow<String?>(null)
    val mevcutSayfa = MutableStateFlow(1)

    private val gosterilenNotlarFlow = combine(secilenKategoriId, mevcutSayfa) { katId, page ->
        Pair(katId, page)
    }.flatMapLatest { (katId, page) ->
        val offset = (page - 1) * PAGE_SIZE
        repository.aktifNotlariSayfaliGetir(kategoriId = katId, limit = PAGE_SIZE + 1, offset = offset)
    }

    val sayfaState: StateFlow<NotlarSayfaState> = combine(
        gosterilenNotlarFlow,
        mevcutSayfa
    ) { items, page ->
        val sonrakiVarMi = items.size > PAGE_SIZE
        val gosterilecekNotlar = if (sonrakiVarMi) items.take(PAGE_SIZE) else items

        NotlarSayfaState(
            gosterilenNotlar = gosterilecekNotlar,
            mevcutSayfa = page,
            sonrakiVarMi = sonrakiVarMi
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotlarSayfaState()
    )

    fun kategoriFiltrele(kategoriId: String?) {
        mevcutSayfa.value = 1
        secilenKategoriId.value = kategoriId
    }

    fun sonrakiSayfa() {
        val currentState = sayfaState.value
        if (currentState.sonrakiVarMi) {
            mevcutSayfa.value = currentState.mevcutSayfa + 1
        }
    }

    fun oncekiSayfa() {
        val currentState = sayfaState.value
        if (currentState.mevcutSayfa > 1) {
            mevcutSayfa.value = currentState.mevcutSayfa - 1
        }
    }

    fun notEkle(baslik: String, aciklama: String, oncelik: String, kategoriId: String, kullaniciId: String) {
        viewModelScope.launch {
            repository.notEkle(baslik, aciklama, metinToSayi(oncelik), kategoriId, kullaniciId)
        }
    }

    fun notDuzenle(not: Not, baslik: String, aciklama: String, oncelik: String, kategoriId: String, kullaniciId: String) {
        viewModelScope.launch {
            repository.notDuzenle(not, baslik, aciklama, metinToSayi(oncelik), kategoriId, kullaniciId)
        }
    }

    fun notSil(not: Not) {
        viewModelScope.launch {
            repository.notSil(not)
        }
    }

    fun notSenkronizeEt() {
        viewModelScope.launch {
            repository.tamSenkronizasyonYap()
        }
    }

    private fun metinToSayi(deger: String): Int {
        var sayi = 1
        if (deger == "Kritik") {
            sayi = 3
        } else if (deger == "Orta") {
            sayi = 2
        }
        return sayi
    }

    fun sayiToMetin(deger: Int): String {
        var metin = "Düşük"
        if (deger == 3) {
            metin = "Kritik"
        } else if (deger == 2) {
            metin = "Orta"
        }
        return metin
    }
}