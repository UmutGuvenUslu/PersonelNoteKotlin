package com.example.personelnotekotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.personelnotekotlin.data.OturumYoneticisi
import com.example.personelnotekotlin.data.GenelSenkron
import com.example.personelnotekotlin.screen.GirisScreen
import com.example.personelnotekotlin.screen.MainScreen
import com.example.personelnotekotlin.ui.theme.PersonelNoteKotlinTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            PersonelNoteKotlinTheme {
                val context = LocalContext.current
                var hazirMi by remember { mutableStateOf(false) }
                val token by OturumYoneticisi.tokenAkisi.collectAsState()

                LaunchedEffect(Unit) {
                    OturumYoneticisi.oturumYukle(context)
                    hazirMi = true
                }

                if (hazirMi) {
                    if (token.isEmpty()) {
                        GirisScreen()
                    } else {

                        LaunchedEffect(token) {
                            GenelSenkron.arkaPlanSenkronBaslat(context)
                        }


                        LaunchedEffect(token) {
                            while (isActive) {
                                try {
                                    GenelSenkron.senkronizeEt(context)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                delay(60_000L)
                            }
                        }

                        MainScreen()
                    }
                }
            }
        }
    }
}