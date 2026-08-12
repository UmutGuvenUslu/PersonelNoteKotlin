package com.example.personelnotekotlin.screen

import AddEditNoteBottomSheet
import android.widget.Space
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.personelnotekotlin.data.Not
import com.example.personelnotekotlin.data.NotRepository
import com.example.personelnotekotlin.data.databaseyiGetir

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var context = LocalContext.current

    var viewModel:NotViewModel =
        viewModel(
        factory = viewModelFactory {
            initializer {
            NotViewModel(NotRepository(databaseyiGetir(context).notDao()))
            }
        }
    )

    val notListesi by viewModel.notListesi.collectAsState()


    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        viewModel.notSenkronizeEt()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Senkronize Et"
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    IconButton(onClick = {
                        showBottomSheet = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ekle"
                        )
                    }

                },
                title = {
                    Text(
                        "Personel Not Uygulaması",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { innerPadding ->
        ScrollContent(innerPadding,notListesi,viewModel)
    }

    if (showBottomSheet) {
        AddEditNoteBottomSheet(
            ilkBaslik = "",
            ilkIcerik = "",
            onKapatRequest = { showBottomSheet = false },
            onKaydetClick = { baslik, icerik, oncelik ->
                viewModel.notEkle(baslik,icerik,oncelik)
                showBottomSheet = false
            }
        )
    }
}

@Composable
fun ScrollContent(
    innerPadding: PaddingValues,
    notListesi:List<Not>,
    viewModel: NotViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding
    ) {
        items(notListesi, key = {it._id}) { not ->
            ListCardComponent(not,viewModel)
        }
    }
}