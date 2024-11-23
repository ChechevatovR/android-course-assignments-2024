package roman.chechevatov.hw2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed interface ImageItem: Parcelable {
    @Parcelize
    class Requested(val height: Int, val width: Int): ImageItem

    @Parcelize
    class Failed(val height: Int, val width: Int, val url: String, val position: Int): ImageItem

    @Parcelize
    data class Fetched(val data: ByteArray): ImageItem
}
