package roman.chechevatov.hw2.client.pixabay

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import roman.chechevatov.hw2.client.pixabay.model.ApiResponse

interface PixabayApi {
    @GET(".")
    suspend fun getImagesInfo(
        @Query("key") apiKey: String,
        @Query("q") searchQuery: String? = null,
        @Query("page") pageNumber: Int = 1,
        @Query("per_page") perPage: Int = 10
    ) : Response<ApiResponse>

    @GET()
    suspend fun getImage(@Url url: String): ResponseBody
}