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
    }

    suspend fun notSil(not: Not){

        if (not.sunucudaVarMi){
            notDao.notSoftSil(not._id)
        }else{
            notDao.notHardSil(not)
        }

    }

    suspend fun notSenkronizeEt(){

        try {
            val bekleyenNotlar = notDao.senkronOlmayanlariGetir()


            for (not in bekleyenNotlar){

                if (not.silindiMi){
                    var yanit = notApi.notSil(not._id)
                    if (yanit.isSuccessful){
                        notDao.notHardSil(not)
                    }
                }else if(!not.sunucudaVarMi){
                    var yanit = notApi.notEkle(not)
                    if (yanit.isSuccessful){
                        notDao.notSenkronizeEt(listOf(not._id))
                    }
                }else{

                        var yanit = notApi.notDuzenle(not._id,not)
                        if (yanit.isSuccessful){
                            notDao.notSenkronizeEt(listOf(not._id))
                        }
                    }


            }



            var yanit = notApi.notGetir()
            if (yanit.isSuccessful){
                var gelenNotlar = yanit.body() ?: emptyList()
                for (not in gelenNotlar){
                    not.senkronMu = true
                    not.sunucudaVarMi = true

                    notDao.notEkleVeyaGuncelle(not)

                }
            }


        }catch (e:Exception){
            e.printStackTrace()
        }



    }


}