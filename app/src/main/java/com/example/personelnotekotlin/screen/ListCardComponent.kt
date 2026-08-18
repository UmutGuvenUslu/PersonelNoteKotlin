package com.example.personelnotekotlin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personelnotekotlin.data.Not

@Composable
fun ListCardComponent(
    not: Not,
    notViewModel: NotViewModel,
    kategoriListesi: List<com.example.personelnotekotlin.data.Kategori> = emptyList()
) {

    var showBottomSheet by remember { mutableStateOf(false) }

    fun oncelikToString(deger: Int): String {
        return when (deger) {
            3 -> "Kritik"
            2 -> "Orta"
            else -> "Düşük"
        }
    }

    fun renk(deger: Int): Color {
        return when (deger) {
            3 -> Color(0xFFE57373) // Kırmızı / Kritik
            2 -> Color(0xFFFFB74D) // Turuncu / Orta
            else -> Color(0xFF81C784) // Yeşil / Düşük
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = not.baslik,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Açıklama: ${not.aciklama}",
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
                    fontStyle = FontStyle.Italic
                )
                val kategoriAdi = kategoriListesi.find { it._id == not.kategoriId }?.isim
                if (!kategoriAdi.isNullOrEmpty()) {
                    Text(
                        text = "Kategori: $kategoriAdi",
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = oncelikToString(not.oncelik),
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = renk(not.oncelik)
                )
            }
            Row(modifier = Modifier.padding(end = 8.dp)) {
                IconButton(onClick = {
                    showBottomSheet = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Notu Düzenle"
                    )
                }
                IconButton(onClick = { notViewModel.notSil(not) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Notu Sil",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        AddEditNoteBottomSheet(
            ilkBaslik = not.baslik,
            ilkIcerik = not.aciklama,
            ilkOncelik = oncelikToString(not.oncelik),
            ilkKategoriId = not.kategoriId,
            ilkKullaniciId = not.kullaniciId,
            kategoriListesi = kategoriListesi,
            onKapatRequest = { showBottomSheet = false },
            onKaydetClick = { baslik, icerik, oncelik, kategoriId, kullaniciId ->
                notViewModel.notDuzenle(not, baslik, icerik, oncelik, kategoriId, kullaniciId)
                showBottomSheet = false
            }
        )
    }
}