package com.example.personelnotekotlin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.personelnotekotlin.data.KategoriRepository
import com.example.personelnotekotlin.data.Not
import com.example.personelnotekotlin.data.NotRepository
import com.example.personelnotekotlin.data.databaseyiGetir

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val database = databaseyiGetir(context)

    val notViewModel: NotViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                NotViewModel(NotRepository(database.notDao()))
            }
        }
    )

    val kategoriViewModel: KategoriViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                KategoriViewModel(KategoriRepository(database.kategoriDao()))
            }
        }
    )

    val sayfaState by notViewModel.sayfaState.collectAsState()
    val kategoriler by kategoriViewModel.kategorilistesi.collectAsState()
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
                        notViewModel.notSenkronizeEt()
                        kategoriViewModel.kategoriSenkronizeEt()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Senkronize Et"
                        )
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            KategoriListComponent(
                kategoriViewModel = kategoriViewModel,
                onCategorySelected = { kategoriId ->
                    notViewModel.kategoriFiltrele(kategoriId)
                }
            )

            if (sayfaState.gosterilenNotlar.isEmpty() && sayfaState.mevcutSayfa == 1) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Henüz not yok.\nYeni not ekleyebilirsiniz.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                ScrollContent(
                    notListesi = sayfaState.gosterilenNotlar,
                    viewModel = notViewModel,
                    kategoriListesi = kategoriler,
                    modifier = Modifier.weight(1f)
                )

                PaginationControls(
                    mevcutSayfa = sayfaState.mevcutSayfa,
                    sonrakiVarMi = sayfaState.sonrakiVarMi,
                    onOncekiClick = { notViewModel.oncekiSayfa() },
                    onSonrakiClick = { notViewModel.sonrakiSayfa() }
                )
            }
        }
    }

    if (showBottomSheet) {
        AddEditNoteBottomSheet(
            ilkBaslik = "",
            ilkIcerik = "",
            kategoriListesi = kategoriler,
            onKapatRequest = { showBottomSheet = false },
            onKaydetClick = { baslik, icerik, oncelik, kategoriId, kullaniciId ->
                notViewModel.notEkle(baslik, icerik, oncelik, kategoriId, kullaniciId)
                showBottomSheet = false
            }
        )
    }
}

@Composable
fun ScrollContent(
    notListesi: List<Not>,
    viewModel: NotViewModel,
    kategoriListesi: List<com.example.personelnotekotlin.data.Kategori> = emptyList(),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            items = notListesi,
            key = { not -> not._id }
        ) { not ->
            ListCardComponent(not, viewModel, kategoriListesi)
        }
    }
}

@Composable
fun PaginationControls(
    mevcutSayfa: Int,
    sonrakiVarMi: Boolean,
    onOncekiClick: () -> Unit,
    onSonrakiClick: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onOncekiClick,
                enabled = mevcutSayfa > 1
            ) {
                Text("Önceki")
            }

            Text(
                text = "Sayfa $mevcutSayfa",
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedButton(
                onClick = onSonrakiClick,
                enabled = sonrakiVarMi
            ) {
                Text("Sonraki")
            }
        }
    }
}