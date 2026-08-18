package com.example.personelnotekotlin.data;

import kotlinx.coroutines.flow.Flow

class KategoriRepository(
         private val kategoriDao: IKategoriDataAccess,
         private val kategoriApi: IKategoriApi = RetrofitClient.kategoriApiService
 ) {

     fun aktifKategorileriGetir(): Flow<List<Kategori>> = kategoriDao.aktifKategorileriGetir()

    suspend fun kategoriEkle(isim:String){

        var yenikategori = Kategori(
            isim = isim,
        )

        kategoriDao.kategoriEkleVeyaGuncelle(yenikategori)
        kategoriSenkronizeEt()
    }

    suspend fun kategoriDuzenle(kategori: Kategori,kategoriName:String){

        var güncelKategori = kategori.copy(
            isim = kategoriName
        )

        kategoriDao.kategoriEkleVeyaGuncelle(güncelKategori)
        kategoriSenkronizeEt()
    }


    suspend fun kategoriSil(kategori: Kategori){

        if (kategori.sunucudaVarMi){
            kategoriDao.kategoriSoftSil(kategori._id)
        }else{
            kategoriDao.kategoriHardSil(kategori)
        }

        kategoriSenkronizeEt()
    }





    suspend fun kategoriSenkronizeEt(){

        try {
            val bekleyenKategoriler = kategoriDao.senkronOlmayanKategorileriGetir()

            if (bekleyenKategoriler.isEmpty()) return

            val yanit = kategoriApi.kategoriSenkronizeEt(bekleyenKategoriler)

            if (yanit.isSuccessful) {
                val senkronizeIdListesi = bekleyenKategoriler.map { it._id }
                kategoriDao.kategoriSenkronizeEt(senkronizeIdListesi)

                for (kategori in bekleyenKategoriler){
                    if(kategori.silindiMi){
                        kategoriDao.kategoriHardSil(kategori)
                    }
                }
            }


        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun sunucudanVerileriGuncelle() {
        try {
            val yanit = kategoriApi.kategorileriGetir()
            if (yanit.isSuccessful) {
                yanit.body()?.let { sunucudakiKategoriler ->
                    val sunucuIdListesi = sunucudakiKategoriler.map { it._id }
                    if (sunucuIdListesi.isEmpty()) {
                        kategoriDao.tumSunucuKategorileriniSil()
                    } else {
                        kategoriDao.sunucudaOlmayanKategorileriSil(sunucuIdListesi)
                    }

                    for (kategori in sunucudakiKategoriler) {
                        val guncelkategori = kategori.copy(senkronMu = true, sunucudaVarMi = true)
                        kategoriDao.kategoriEkleVeyaGuncelle(guncelkategori)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun tamSenkronizasyonYap() {
        kategoriSenkronizeEt()
        sunucudanVerileriGuncelle()
    }

}

