package roman.chechevatov.hw1

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ColoredSquaresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ColoredSquaresAdapter(context = this)
        adapter.loadInstanceState(savedInstanceState)

        findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter
        findViewById<Button>(R.id.add_element_button).setOnClickListener {
            adapter.addItem()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        adapter.saveInstanceState(outState)
    }
}