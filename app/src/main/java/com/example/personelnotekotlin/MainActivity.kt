package com.example.personelnotekotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.example.personelnotekotlin.data.SyncWorker
import com.example.personelnotekotlin.screen.MainScreen
import com.example.personelnotekotlin.ui.theme.PersonelNoteKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(1,TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NotSenkron",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        enableEdgeToEdge()
        setContent {
            PersonelNoteKotlinTheme {
                MainScreen()
                }
            }
        }
    }


