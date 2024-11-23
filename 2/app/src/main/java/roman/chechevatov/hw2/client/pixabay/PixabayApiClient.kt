package roman.chechevatov.hw2.client.pixabay

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import roman.chechevatov.hw2.client.pixabay.model.ApiResponse

class PixabayApiClient(private val apiKey: String, private val searchQuery: String = "airplane", baseUrl: String = "https://pixabay.com/api/") {
    private val pixabayApi: PixabayApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PixabayApi::class.java);


    suspend fun get(pageNumber: Int): ApiResponse {
        val info = pixabayApi.getImagesInfo(apiKey, searchQuery = searchQuery, pageNumber = pageNumber)
        return info.body()!!
    }

    suspend fun getImage(url: String): ByteArray {
        return pixabayApi.getImage(url).bytes()
    }
}