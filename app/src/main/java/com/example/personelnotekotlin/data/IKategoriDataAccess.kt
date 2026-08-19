package com.example.personelnotekotlin.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IKategoriDataAccess {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun kategoriEkleVeyaGuncelle(kategori: Kategori)

        @Query("UPDATE `KATEGORI` SET silindiMi = 1,senkronMu = 0,guncellemeTarihi = :guncellemeTarihi WHERE _id = :kategoriid")
        suspend fun kategoriSoftSil(kategoriid:String,guncellemeTarihi:Long = System.currentTimeMillis())

        @Delete
        suspend fun kategoriHardSil(kategori: Kategori)

        @Query("UPDATE `kategori` SET senkronMu = 1, sunucudaVarMi = 1 WHERE _id IN (:kategoriidleri)")
        suspend fun kategoriSenkronizeEt(kategoriidleri:List<String>)

        @Query("SELECT * FROM `kategori` WHERE silindiMi = 0 ORDER BY guncellemeTarihi DESC ")
        fun aktifKategorileriGetir(): Flow<List<Kategori>>

        @Query("SELECT * FROM `kategori` WHERE senkronMu = 0 ORDER BY guncellemeTarihi ASC")
        suspend fun senkronOlmayanKategorileriGetir():List<Kategori>

        @Query("DELETE FROM `kategori` WHERE sunucudaVarMi = 1 AND senkronMu = 1 AND _id NOT IN (:sunucudakiIdler)")
        suspend fun sunucudaOlmayanKategorileriSil(sunucudakiIdler: List<String>)

        @Query("DELETE FROM `kategori` WHERE sunucudaVarMi = 1 AND senkronMu = 1")
        suspend fun tumSunucuKategorileriniSil()


    }
