package roman.chechevatov.hw2

import android.content.ContentResolver
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import roman.chechevatov.hw2.client.pixabay.PixabayApiClient
import roman.chechevatov.hw2.model.ImageItem
import roman.chechevatov.hw2.service.ImageProviderService
import roman.chechevatov.hw2.service.PixabayImageProvider

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ImageItemsAdapter
    private lateinit var loadMoreButton: Button
    private lateinit var loadingSpinner: ProgressBar
    private var pageNumber = 1
    private val imageProvider: ImageProviderService =
        PixabayImageProvider(PixabayApiClient("47128403-639fea9715205e582feebb26b"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ImageItemsAdapter(this)
        adapter.loadInstanceState(savedInstanceState)

        findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter

        loadMoreButton = findViewById(R.id.load_more_button)
        loadMoreButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                addPageOfImages()
            }
        }
        loadingSpinner = findViewById(R.id.loading_bar)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        adapter.saveInstanceState(outState)
    }

    private suspend fun addPageOfImages() {
        this.contentResolver.q
        showLoadingSpinner()
        val urls = CoroutineScope(Dispatchers.IO).async {
            return@async try {
                imageProvider.getImageUrls(pageNumber).also { pageNumber++ }
            } catch (e: ImageProviderService.ImageException) {
                listOf()
            }
        }.await()
        urls.forEach {
            addOneImage(it.url, it.height, it.width)
        }
        showLoadMoreButton()
    }

    private suspend fun addOneImage(url: String, height: Int, width: Int) {
        val updateItemCallback = adapter.addItem(ImageItem.Requested(height, width))
        reloadImage(url, height, width, updateItemCallback)
    }



    private fun showLoadMoreButton() {
        CoroutineScope(Dispatchers.Main).launch {
            loadMoreButton.visibility = View.VISIBLE
            loadingSpinner.visibility = View.GONE
        }
    }

    private fun showLoadingSpinner() {
        CoroutineScope(Dispatchers.Main).launch {
            loadMoreButton.visibility = View.GONE
            loadingSpinner.visibility = View.VISIBLE
        }
    }
}