package roman.chechevatov.hw2.service

import android.graphics.drawable.Drawable
import roman.chechevatov.hw2.model.ImageInfo

interface ImageProviderService {
    suspend fun getImageUrls(pageNumber: Int): List<ImageInfo>
    suspend fun getImage(url: String): ByteArray

    class ImageException(cause: Throwable) : RuntimeException(cause)
}