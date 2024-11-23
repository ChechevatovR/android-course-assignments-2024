package roman.chechevatov.hw2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import roman.chechevatov.hw2.model.ImageItem

class ImageItemsAdapter(
    private val mainActivity: MainActivity,
    private val items: MutableList<ImageItem> = mutableListOf()
) : RecyclerView.Adapter<ImageItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        return ImageItemViewHolder(
            mainActivity,
            LayoutInflater.from(parent.context).inflate(R.layout.image_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    suspend fun addItem(item: ImageItem): Int {
        val position = CoroutineScope(Dispatchers.Main).async {
            val position = items.size
            items.add(item)
            notifyItemInserted(position)
            return@async position
        }.await()
        return position
    }

    fun updateItem(position: Int, item: ImageItem) {
        CoroutineScope(Dispatchers.Main).launch {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun loadInstanceState(savedState: Bundle?) {
        savedState?.getParcelable("AAA", ParcelItems::class.java)?.let {
            val sizeBefore = items.size
            items.clear()
            notifyItemRangeChanged(0, sizeBefore)
            items.addAll(it.items)
            notifyItemRangeInserted(0, items.size)
        }
    }

    fun saveInstanceState(outState: Bundle) {
        outState.putParcelable("AAA", ParcelItems(items))
    }

    @Parcelize
    data class ParcelItems(val items: List<ImageItem>) : Parcelable
}