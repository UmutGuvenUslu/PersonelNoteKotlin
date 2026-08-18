package com.example.personelnotekotlin.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

class  NotRepository(
    private val notDao: INotDataAccess,
    private val notApi: INotApi = RetrofitClient.notApiService
){

    fun aktifNotlariGetir(): Flow<List<Not>> = notDao.aktifNotlariGetir()

    suspend fun notEkle(baslik:String,aciklama:String,oncelik:Int,kategori: String,kullanici: String){
        var yeniNot = Not(
            baslik = baslik,
            aciklama = aciklama,
            oncelik = oncelik,
            kullaniciId = kullanici,
            kategoriId = kategori
        )
        notDao.notEkleVeyaGuncelle(yeniNot)
        notSenkronizeEt()
    }

    suspend fun notDuzenle(not: Not,baslik:String,aciklama:String,oncelik:Int,kategori: String,kullanici: String){
        //rol
        var guncelNot = not.copy(
            baslik = baslik,
            aciklama = aciklama,
            oncelik = oncelik,
            kullaniciId = kullanici,
            kategoriId = kategori,
            guncellemeTarihi = System.currentTimeMillis(),
            senkronMu = false
        )
        notDao.notEkleVeyaGuncelle(guncelNot)
        notSenkronizeEt()
    }

    suspend fun notSil(not: Not){

        if (not.sunucudaVarMi){
            notDao.notSoftSil(not._id)
        }else{
            notDao.notHardSil(not)
        }
        notSenkronizeEt()
    }

    suspend fun notSenkronizeEt() {
        try {
            val bekleyenNotlar = notDao.senkronOlmayanlariGetir()

            if (bekleyenNotlar.isEmpty()) return

            val yanit = notApi.notSenkronizeEt(bekleyenNotlar)

            if (yanit.isSuccessful) {
                val senkronizeIdListesi = bekleyenNotlar.map { it._id }
                notDao.notSenkronizeEt(senkronizeIdListesi)

                for (not in bekleyenNotlar){
                    if(not.silindiMi){
                        notDao.notHardSil(not)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun sunucudanVerileriGuncelle() {
        try {
            val yanit = notApi.notGetir()
            if (yanit.isSuccessful) {
                yanit.body()?.let { sunucudakiNotlar ->
                    val sunucuIdListesi = sunucudakiNotlar.map { it._id }
                    if (sunucuIdListesi.isEmpty()) {
                        notDao.tumSunucuNotlariniSil()
                    } else {
                        notDao.sunucudaOlmayanNotlariSil(sunucuIdListesi)
                    }

                    for (not in sunucudakiNotlar) {
                        val guncelNot = not.copy(senkronMu = true, sunucudaVarMi = true)
                        notDao.notEkleVeyaGuncelle(guncelNot)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun tamSenkronizasyonYap() {
        notSenkronizeEt()

        sunucudanVerileriGuncelle()
    }

}