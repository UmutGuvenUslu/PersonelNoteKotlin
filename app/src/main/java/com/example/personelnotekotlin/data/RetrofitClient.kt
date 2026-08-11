import com.example.personelnotekotlin.data.INotApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val  apiService : INotApi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:1880/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(INotApi::class.java)
}