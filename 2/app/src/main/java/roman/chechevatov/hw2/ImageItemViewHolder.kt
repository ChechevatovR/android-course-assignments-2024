package roman.chechevatov.hw2

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import roman.chechevatov.hw2.model.ImageItem

class ImageItemViewHolder(private val mainActivity: MainActivity, view: View) : RecyclerView.ViewHolder(view) {

    private val layout: FrameLayout = view.findViewById(R.id.card_layout_no_image)
    private val loading: ProgressBar = view.findViewById(R.id.loading_bar)
    private val image: ImageView = view.findViewById(R.id.image)
    private val retryButton: Button = view.findViewById(R.id.retry_button)

    fun bind(item: ImageItem) {
        when (item) {
            is ImageItem.Failed -> {
                (layout.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "${item.width}:${item.height}"
                layout.visibility = View.VISIBLE
                image.visibility = View.GONE
                loading.visibility = View.GONE
                retryButton.visibility = View.VISIBLE
                retryButton.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        mainActivity.reloadImage(item.url, item.height, item.width, item.position)
                    }
                }
            }

            is ImageItem.Fetched -> {
                image.setImageDrawable(BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(item.data, 0, item.data.size)))
                image.visibility = View.VISIBLE
                layout.visibility = View.GONE
            }

            is ImageItem.Requested -> {
                (layout.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "${item.width}:${item.height}"
                layout.visibility = View.VISIBLE
                loading.visibility = View.VISIBLE
                image.visibility = View.GONE
                retryButton.visibility = View.GONE
            }
        }
    }
}