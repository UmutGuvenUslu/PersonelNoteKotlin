package com.example.personelnotekotlin.data

import RetrofitClient
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

class  NotRepository(
    private val notDao: INotDataAccess,
    private val notApi: INotApi = RetrofitClient.apiService
){

    fun aktifNotlariGetir(): Flow<List<Not>> = notDao.aktifNotlariGetir()

    suspend fun notEkle(baslik:String,aciklama:String,oncelik:Int){
        var yeniNot = Not(
            baslik = baslik,
            aciklama = aciklama,
            oncelik = oncelik
        )
        notDao.notEkleVeyaGuncelle(yeniNot)
        notSenkronizeEt()
    }

    suspend fun notDuzenle(not: Not,baslik:String,aciklama:String,oncelik:Int){
        var guncelNot = not.copy(
            baslik = baslik,
            aciklama = aciklama,
            oncelik = oncelik,
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


}