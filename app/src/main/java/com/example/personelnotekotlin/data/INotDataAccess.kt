package com.example.personelnotekotlin.data


import androidx.paging.PagingSource
import androidx.room.OnConflictStrategy
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface INotDataAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun notEkleVeyaGuncelle(not:Not)

    @Query("UPDATE `Not` SET silindiMi = 1,senkronMu = 0,guncellemeTarihi = :guncellemeTarihi WHERE _id = :notid")
    suspend fun notSoftSil(notid:String,guncellemeTarihi:Long = System.currentTimeMillis())

    @Delete
    suspend fun notHardSil(not: Not)

    @Query("UPDATE `Not` SET senkronMu = 1, sunucudaVarMi = 1 WHERE _id IN (:notidleri)")
    suspend fun notSenkronizeEt(notidleri:List<String>)

    @Query("SELECT * FROM `Not` WHERE silindiMi = 0 AND (:adminMi = 1 OR kullaniciId = :kullaniciId) AND (:kategoriId IS NULL OR :kategoriId = '' OR kategoriId = :kategoriId) ORDER BY oncelik DESC, guncellemeTarihi DESC LIMIT :limit OFFSET :offset")
    fun aktifNotlariSayfaliGetir(adminMi: Int = 0, kullaniciId: String = "", kategoriId: String? = null, limit: Int = 20, offset: Int = 0): Flow<List<Not>>

    @Query("SELECT * FROM `Not` WHERE senkronMu = 0 ORDER BY guncellemeTarihi ASC")
    suspend fun senkronOlmayanlariGetir():List<Not>

    @Query("DELETE FROM `Not` WHERE sunucudaVarMi = 1 AND senkronMu = 1 AND _id NOT IN (:sunucudakiIdler)")
    suspend fun sunucudaOlmayanNotlariSil(sunucudakiIdler: List<String>)

    @Query("DELETE FROM `Not` WHERE sunucudaVarMi = 1 AND senkronMu = 1")
    suspend fun tumSunucuNotlariniSil()


}
