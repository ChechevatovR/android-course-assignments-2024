package roman.chechevatov.hw1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ColoredSquareViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val text: TextView = view.findViewById(R.id.text)

    fun bind(number: Int, backgroundColor: Int) {
        text.text = "$number"
        text.setBackgroundColor(backgroundColor)
    }
}