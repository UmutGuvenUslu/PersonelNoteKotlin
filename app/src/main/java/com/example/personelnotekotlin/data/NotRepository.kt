package com.example.personelnotekotlin.data

import kotlinx.coroutines.flow.Flow

class  NotRepository(
    private val notDao: INotDataAccess,
    private val notApi: INotApi = RetrofitClient.notApiService
){

    fun aktifNotlariSayfaliGetir(
        kategoriId: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): Flow<List<Not>> {

        var adminMi = 0

        if (OturumYoneticisi.adminMi()){
            adminMi = 1
        }

        return notDao.aktifNotlariSayfaliGetir(adminMi,OturumYoneticisi.kullaniciId,kategoriId,limit,offset)
    }

    suspend fun notEkle(baslik:String,aciklama:String,oncelik:Int,kategori: String,kullanici: String){

        var atanan = OturumYoneticisi.kullaniciId

        if (OturumYoneticisi.adminMi() && kullanici != ""){
            atanan = kullanici
        }

        var yeniNot = Not(
            baslik = baslik,
            aciklama = aciklama,
            oncelik = oncelik,
            kullaniciId = atanan,
            kategoriId = kategori
        )
        notDao.notEkleVeyaGuncelle(yeniNot)
        notSenkronizeEt()
    }

    suspend fun notDuzenle(not: Not,baslik:String,aciklama:String,oncelik:Int,kategori: String,kullanici: String){
        //rol
        var atanan = OturumYoneticisi.kullaniciId

        if (OturumYoneticisi.adminMi() && kullanici != ""){
            atanan = kullanici
        }

        var guncelNot = not.copy(
            baslik = baslik,
            aciklama = aciklama,
            oncelik = oncelik,
            kullaniciId = atanan,
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
            val tumSunucudakiNotlar = mutableListOf<Not>()
            var sayfa = 1
            while (true) {
                val yanit = notApi.notGetir(sayfa = sayfa)
                if (yanit.isSuccessful) {
                    val gelenNotlar = yanit.body()
                    if (gelenNotlar.isNullOrEmpty()) {
                        break
                    }
                    tumSunucudakiNotlar.addAll(gelenNotlar)
                    if (gelenNotlar.size < 10) {
                        break
                    }
                    sayfa++
                } else {
                    break
                }
            }

            val sunucuIdListesi = tumSunucudakiNotlar.map { it._id }
            if (sunucuIdListesi.isEmpty()) {
                notDao.tumSunucuNotlariniSil()
            } else {
                notDao.sunucudaOlmayanNotlariSil(sunucuIdListesi)
            }

            for (not in tumSunucudakiNotlar) {
                val guncelNot = not.copy(senkronMu = true, sunucudaVarMi = true)
                notDao.notEkleVeyaGuncelle(guncelNot)
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
