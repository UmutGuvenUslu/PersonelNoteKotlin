package com.example.personelnotekotlin.data

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object GenelSenkron {

    suspend fun senkronizeEt(context: Context) {
        OturumYoneticisi.oturumYukle(context)
        if (OturumYoneticisi.tokenGetir().isEmpty()) return

        val database = databaseyiGetir(context)

        val kategoriRepository = KategoriRepository(database.kategoriDao())
        kategoriRepository.tamSenkronizasyonYap()

        val notRepository = NotRepository(database.notDao())
        notRepository.tamSenkronizasyonYap()
    }

    fun arkaPlanSenkronBaslat(context: Context) {
        val periodicRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "ArkaPlanSenkronIsi",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }
}