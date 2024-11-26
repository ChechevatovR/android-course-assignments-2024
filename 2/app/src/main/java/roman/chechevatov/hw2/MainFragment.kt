package roman.chechevatov.hw2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import roman.chechevatov.hw2.client.pixabay.PixabayApiClient
import roman.chechevatov.hw2.model.ImageItem
import roman.chechevatov.hw2.service.ImageProviderService
import roman.chechevatov.hw2.service.PixabayImageProvider

private const val PAGE_NUMBER_BUNDLE_KEY = "roman.chechevatov.hw2.MainFragment.pageNumber"

class MainFragment: Fragment(R.layout.fragment_main) {
    private lateinit var adapter: ImageItemsAdapter
    private lateinit var loadMoreButton: Button
    private lateinit var loadingSpinner: ProgressBar
    private var pageNumber = 1
    private lateinit var imageProvider: ImageProviderService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageProvider = PixabayImageProvider(PixabayApiClient(getString(R.string.pixabay_api_key), baseUrl = getString(R.string.pixabay_endpoint)))
        adapter = ImageItemsAdapter(this)
        adapter.loadInstanceState(savedInstanceState)
        savedInstanceState?.let {
            pageNumber = it.getInt(PAGE_NUMBER_BUNDLE_KEY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter

        loadMoreButton = view.findViewById(R.id.load_more_button)
        loadMoreButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                addPageOfImages()
            }
        }
        loadingSpinner = view.findViewById(R.id.loading_bar)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PAGE_NUMBER_BUNDLE_KEY, pageNumber)
        adapter.saveInstanceState(outState)
    }

    private suspend fun addPageOfImages() {
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

    fun reloadImage(url: String, height: Int, width: Int, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                adapter.updateItem(position, ImageItem.Requested(height, width))
                val drawable = imageProvider.getImage(url)
                adapter.updateItem(position, ImageItem.Fetched(drawable))
            } catch (e: ImageProviderService.ImageException) {
                adapter.updateItem(position, ImageItem.Failed(height, width, url, position))
            }
        }
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