package com.example.personelnotekotlin.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personelnotekotlin.data.Kategori

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteBottomSheet(
    ilkBaslik: String = "",
    ilkIcerik: String = "",
    ilkOncelik: String = "Düşük",
    ilkKategoriId: String = "",
    ilkKullaniciId: String = "",
    kategoriListesi: List<Kategori> = emptyList(),
    onKapatRequest: () -> Unit,
    onKaydetClick: (baslik: String, icerik: String, oncelik: String, kategoriId: String, kullaniciId: String) -> Unit
) {
    val sheetDurumu = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var baslik by remember { mutableStateOf(ilkBaslik) }
    var icerik by remember { mutableStateOf(ilkIcerik) }
    var secilenOncelik by remember { mutableStateOf(ilkOncelik) }
    var kategoriId by remember { mutableStateOf(ilkKategoriId) }
    var kullaniciId by remember { mutableStateOf(ilkKullaniciId) }
    var dropdownAcikMi by remember { mutableStateOf(false) }

    val oncelikListesi = listOf("Düşük", "Orta", "Kritik")

    ModalBottomSheet(
        onDismissRequest = onKapatRequest,
        sheetState = sheetDurumu
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (ilkBaslik.isEmpty()) "Yeni Not Ekle" else "Notu Düzenle",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = baslik,
                onValueChange = { baslik = it },
                label = { Text("Başlık") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = icerik,
                onValueChange = { icerik = it },
                label = { Text("Not Detayı") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            val secilenKategoriIsmi = kategoriListesi.find { it._id == kategoriId }?.isim ?: kategoriId

            ExposedDropdownMenuBox(
                expanded = dropdownAcikMi,
                onExpandedChange = { dropdownAcikMi = !dropdownAcikMi }
            ) {
                OutlinedTextField(
                    value = secilenKategoriIsmi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategori") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownAcikMi) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = dropdownAcikMi,
                    onDismissRequest = { dropdownAcikMi = false }
                ) {
                    if (kategoriListesi.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Kategori Bulunamadı") },
                            onClick = { dropdownAcikMi = false }
                        )
                    } else {
                        kategoriListesi.forEach { kategori ->
                            DropdownMenuItem(
                                text = { Text(kategori.isim) },
                                onClick = {
                                    kategoriId = kategori._id
                                    dropdownAcikMi = false
                                }
                            )
                        }
                    }
                }
            }

            Text(
                text = "Öncelik Seviyesi",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                oncelikListesi.forEach { oncelik ->
                    val secildiMi = secilenOncelik == oncelik

                    val arkaPlanRengi = when (oncelik) {
                        "Kritik" -> if (secildiMi) Color(0xFFE57373) else Color(0xFFFFEBEE)
                        "Orta" -> if (secildiMi) Color(0xFFFFB74D) else Color(0xFFFFF3E0)
                        else -> if (secildiMi) Color(0xFF81C784) else Color(0xFFE8F5E9)
                    }

                    val metinRengi = if (secildiMi) Color.White else Color.DarkGray

                    Surface(
                        onClick = { secilenOncelik = oncelik },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = arkaPlanRengi
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = oncelik,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (secildiMi) FontWeight.Bold else FontWeight.Normal,
                                color = metinRengi
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onKaydetClick(baslik, icerik, secilenOncelik, kategoriId, kullaniciId)
                    onKapatRequest()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kaydet")
            }
        }
    }
}