package com.example.personelnotekotlin.screen

import AddEditNoteBottomSheet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personelnotekotlin.data.Not

@Composable
fun ListCardComponent(not: Not,notViewModel: NotViewModel) {

    var showBottomSheet by remember { mutableStateOf(false) }
    fun oncelikToString(deger: Int):String{
        if (deger == 2){
            return "Orta"
        }else if (deger == 3){
            return "Kritik"
        }
        return "Düşük"
    }

    fun renk(deger:Int):Color{
        if (deger == 2){
            return Color(0xFFFFB74D)
        }else if (deger == 3){
            return Color.Red
        }
        return Color.Green
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
                    text = "${not.baslik}",
                    modifier = Modifier.padding(8.dp,16.dp,0.dp,0.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                    Text(
                        text = " Açıklama: ${not.aciklama}",
                        modifier = Modifier.padding(8.dp),
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = oncelikToString(not.oncelik),
                        modifier = Modifier.padding(20.dp,8.dp),
                        color = renk(not.oncelik)

                    )
                }
                Row {
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
                ilkOncelik = notViewModel.sayiToMetin(not.oncelik),
                onKapatRequest = { showBottomSheet = false },
                onKaydetClick = { baslik, icerik, oncelik ->
                    notViewModel.notDuzenle(not,baslik,icerik,oncelik)
                    showBottomSheet = false
                }
            )

        }

}