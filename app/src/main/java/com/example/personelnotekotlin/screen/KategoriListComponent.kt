package com.example.personelnotekotlin.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriListComponent(
    kategoriViewModel: KategoriViewModel,
    onCategorySelected: (String) -> Unit
) {
    val kategoriler by kategoriViewModel.kategorilistesi.collectAsState()
    var secilenKategoriId by remember { mutableStateOf("") }
    var dropdownAcikMi by remember { mutableStateOf(false) }

    val secilenKategoriIsmi = if (secilenKategoriId.isEmpty()) "Tüm Kategoriler" else (kategoriler.find { it._id == secilenKategoriId }?.isim ?: "Tüm Kategoriler")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = dropdownAcikMi,
            onExpandedChange = { dropdownAcikMi = !dropdownAcikMi }
        ) {
            OutlinedTextField(
                value = secilenKategoriIsmi,
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategori Filtresi") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownAcikMi) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = dropdownAcikMi,
                onDismissRequest = { dropdownAcikMi = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Tüm Kategoriler") },
                    onClick = {
                        secilenKategoriId = ""
                        onCategorySelected("")
                        dropdownAcikMi = false
                    }
                )
                kategoriler.forEach { kategori ->
                    DropdownMenuItem(
                        text = { Text(kategori.isim) },
                        onClick = {
                            secilenKategoriId = kategori._id
                            onCategorySelected(kategori._id)
                            dropdownAcikMi = false
                        }
                    )
                }
            }
        }
    }
}