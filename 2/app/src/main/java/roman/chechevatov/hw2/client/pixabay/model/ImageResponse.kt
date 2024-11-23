package roman.chechevatov.hw2.client.pixabay.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    val id: Long,

    @SerializedName("webformatURL")
    val url: String,

    @SerializedName("webformatHeight")
    val height: Int,

    @SerializedName("webformatWidth")
    val width: Int
) {
}