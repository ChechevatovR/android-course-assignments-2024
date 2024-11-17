package roman.chechevatov.hw1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

private const val ITEM_COUNT_PROPERTY_KEY = "roman.chechevatov.hw1.ColoredSquaresAdapter.itemCount"

class ColoredSquaresAdapter(private var itemCount: Int = 0, context: Context) :
    RecyclerView.Adapter<ColoredSquareViewHolder>() {

    private val colorOdd = ContextCompat.getColor(context, R.color.color_odd)
    private val colorEven = ContextCompat.getColor(context, R.color.color_even)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColoredSquareViewHolder {
        return ColoredSquareViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onBindViewHolder(holder: ColoredSquareViewHolder, position: Int) {
        holder.bind(position, getColorForPosition(position))
    }

    private fun getColorForPosition(position: Int): Int {
        return when (getItemViewType(position)) {
            0 -> colorEven
            1 -> colorOdd
            else -> {
                throw IllegalStateException("Unknown item view type")
            }
        }
    }

    fun addItem() {
        notifyItemInserted(itemCount++)
    }

    fun loadInstanceState(savedState: Bundle?) {
        savedState?.getInt(ITEM_COUNT_PROPERTY_KEY)?.let {
            if (it > itemCount) {
                notifyItemRangeInserted(itemCount + 1, it - itemCount)
            } else if (it < itemCount) {
                notifyItemRangeRemoved(it + 1, itemCount - it)
            }
            itemCount = it
        }
    }

    fun saveInstanceState(outState: Bundle) {
        outState.putInt(ITEM_COUNT_PROPERTY_KEY, itemCount)
    }
}