package com.example.personelnotekotlin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GirisScreen() {
    val context = LocalContext.current
    val girisViewModel: GirisViewModel = viewModel()

    var kullaniciAdi by remember { mutableStateOf("") }
    var sifre by remember { mutableStateOf("") }

    val yukleniyor by girisViewModel.yukleniyor.collectAsState()
    val hata by girisViewModel.hata.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Personel Not Uygulaması",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Giriş Yap",
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = kullaniciAdi,
            onValueChange = { kullaniciAdi = it },
            label = { Text("Kullanıcı Adı") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = sifre,
            onValueChange = { sifre = it },
            label = { Text("Şifre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        if (hata != "") {
            Text(
                text = hata,
                modifier = Modifier.padding(top = 12.dp),
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = { girisViewModel.girisYap(context,kullaniciAdi,sifre) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            enabled = !yukleniyor && kullaniciAdi != "" && sifre != ""
        ) {
            if (yukleniyor) {
                Text("Giriş yapılıyor...")
            } else {
                Text("Giriş Yap")
            }
        }
    }
}
