package com.example.personelnotekotlin.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personelnotekotlin.data.GirisIstek
import com.example.personelnotekotlin.data.OturumYoneticisi
import com.example.personelnotekotlin.data.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GirisViewModel:ViewModel()
{

    var yukleniyor = MutableStateFlow(false)
    var hata = MutableStateFlow("")

    fun girisYap(context: Context,kullaniciAdi:String,sifre:String){
        viewModelScope.launch {

            yukleniyor.value = true
            hata.value = ""

            try {
                var yanit = RetrofitClient.authApiService.girisYap(GirisIstek(kullaniciAdi,sifre))

                if (yanit.isSuccessful && yanit.body() != null){
                    var gelen = yanit.body()!!

                    OturumYoneticisi.oturumKaydet(
                        context,
                        gelen.token,
                        gelen.kullanici._id,
                        gelen.kullanici.rol,
                        "${gelen.kullanici.isim} ${gelen.kullanici.soyisim}"
                    )
                }else{
                    hata.value = "Kullanıcı adı veya şifre hatalı."
                }
            }catch (e:Exception){
                e.printStackTrace()
                hata.value = "Sunucuya bağlanılamadı."
            }

            yukleniyor.value = false
        }
    }

}
