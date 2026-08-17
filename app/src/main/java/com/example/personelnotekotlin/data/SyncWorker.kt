package com.example.personelnotekotlin.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SyncWorker(
    context: Context,
    params:WorkerParameters,
):CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {
        return try {
            var database = databaseyiGetir(applicationContext)

            var kategoriRepository = KategoriRepository(database.kategoriDao())
            kategoriRepository.tamSenkronizasyonYap()

            var notRepository = NotRepository(database.notDao())
            notRepository.tamSenkronizasyonYap()

            Result.success()
        }catch (e:Exception){
            e.printStackTrace()
            Result.retry()
        }
    }

}