import com.example.personelnotekotlin.data.IKategoriApi
import com.example.personelnotekotlin.data.INotApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("ngrok-skip-browser-warning", "true")
                .build()
            chain.proceed(newRequest)
        }
        .build()

    val apiService: Retrofit = Retrofit.Builder()
        .baseUrl("https://b44e-2a00-1d34-d4a0-5300-259a-bd63-977e-53d5.ngrok-free.app/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val notApiService: INotApi = apiService.create(INotApi::class.java)
    val kategoriApiService: IKategoriApi = apiService.create(IKategoriApi::class.java)
}