package roman.chechevatov.hw2.service

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import roman.chechevatov.hw2.client.pixabay.PixabayApiClient
import roman.chechevatov.hw2.model.ImageInfo

class PixabayImageProvider(private val client: PixabayApiClient): ImageProviderService {
    override suspend fun getImageUrls(pageNumber: Int): List<ImageInfo> {
        try {
            return client.get(pageNumber).hits.map { ImageInfo(it.url, it.height, it.width) }
        } catch (e: Exception) {
            throw ImageProviderService.ImageException(e)
        }
    }

    override suspend fun getImage(url: String): ByteArray {
        try {
            return client.getImage(url)
        } catch (e: Exception) {
            throw ImageProviderService.ImageException(e)
        }
    }
}