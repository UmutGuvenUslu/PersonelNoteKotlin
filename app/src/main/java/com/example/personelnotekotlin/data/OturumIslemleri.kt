package com.example.personelnotekotlin.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

fun senkronBaslat(context: Context){

    var syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(1,TimeUnit.MINUTES)
        .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "NotSenkron",
        ExistingPeriodicWorkPolicy.UPDATE,
        syncRequest
    )
}

suspend fun cikisYap(context: Context){

    WorkManager.getInstance(context).cancelUniqueWork("NotSenkron")

    withContext(Dispatchers.IO){
        databaseyiGetir(context).clearAllTables()
    }

    OturumYoneticisi.oturumTemizle(context)
}
