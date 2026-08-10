package com.example.personelnotekotlin.screen

import AddEditNoteBottomSheet
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp

@Composable
fun ListCardComponent(index: Int) {

    var showBottomSheet by remember { mutableStateOf(false) }

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
            Text(
                text = "Not ${index + 1}: Liste Deneme",
                modifier = Modifier.padding(16.dp)
            )
            Row {
                IconButton(onClick = {
                    showBottomSheet = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Notu Düzenle"
                    )
                }
                IconButton(onClick = { /* Silme fonksiyonu */ }) {
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
            ilkBaslik = "Not ${index + 1}",
            ilkIcerik = "Liste Deneme",
            onKapatRequest = { showBottomSheet = false },
            onKaydetClick = { baslik, icerik, oncelik ->
                showBottomSheet = false
            }
        )
    }
}