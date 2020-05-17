package app.thdev.photosample.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.thdev.photosample.R
import app.thdev.photosample.data.PhotoItem
import app.thdev.photosample.network.FlickrModule
import app.thdev.photosample.ui.CustomRecyclerView
import app.thdev.photosample.ui.detail.DetailActivity
import app.thdev.photosample.ui.main.adapter.PhotoViewHolder
import app.thdev.photosample.ui.main.presenter.MainContract
import app.thdev.photosample.ui.main.presenter.MainPresenter
import app.thdev.photosample.util.createBlurImage
import tech.thdev.base.ui.BasePresenterFragment
import tech.thdev.simpleadapter.SimpleRecyclerViewAdapter

/**
 * Created by Tae-hwan on 7/21/16.
 */
class MainFragment : BasePresenterFragment<MainContract.View, MainContract.Presenter>(),
    MainContract.View {

    companion object {
        private const val REQ_MAIN_BLUR = 1000
    }

    private val adapter: SimpleRecyclerViewAdapter by lazy {
        SimpleRecyclerViewAdapter { _ ->
            PhotoViewHolder(
                parent = this,
                onClick = presenter::onClickItem,
                onLongClick = presenter::onLongClick
            )
        }
    }

    private val recyclerView by lazy {
        view!!.findViewById<CustomRecyclerView>(R.id.recycler_view)
    }

    private val container by lazy {
        requireActivity().findViewById<View>(R.id.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePresenter() =
        MainPresenter(
            FlickrModule(),
            adapter
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.run {
            adapter = this@MainFragment.adapter
            addOnScrollListener(infiniteScrollListener)
        }

        presenter.init()
    }

    override fun reloadAdapter() {
        adapter.notifyDataSetChanged()
    }

    override fun showDetailView(photo: PhotoItem) {
        DetailActivity.newIntent(requireContext(), photo).apply {
            startActivity(this)
        }
    }

    override fun showBlurDialog(imageUrl: String) {
        recyclerView.isLock = true
        tryGetBackgroundImage {
            MainBlurData.run {
                bitmap = it
            }
            startActivityForResult(
                MainBlurActivity.newIntent(requireContext(), imageUrl),
                REQ_MAIN_BLUR
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_MAIN_BLUR -> {
                recyclerView.isLock = false
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Root capture...
     */
    private fun tryGetBackgroundImage(body: (bitmap: Bitmap) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val width = container.width
            val height = container.height

            val bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
            val locationOfViewInWindow = IntArray(2)

            container.setLayerType(View.LAYER_TYPE_HARDWARE, null)

            PixelCopy.request(
                requireActivity().window,
                Rect(
                    locationOfViewInWindow[0],
                    locationOfViewInWindow[1],
                    locationOfViewInWindow[0] + width,
                    locationOfViewInWindow[1] + height
                ),
                bitmap,
                { copyResult ->
                    when (copyResult) {
                        PixelCopy.SUCCESS -> body(bitmap.createBlurImage(requireContext()))
                        else -> Log.e("TEMP", "copy fail")
                    }
                },
                Handler()
            )
        } else {
            container.isDrawingCacheEnabled = true
            container.buildDrawingCache(true)
            container.drawingCache.createBlurImage(requireContext()).let {
                body(it)
            }
            container.isDrawingCacheEnabled = false
        }
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun showFailLoad() {
        Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.destroy()

        recyclerView.removeOnScrollListener(infiniteScrollListener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchView: SearchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.searchPhotos(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.searchPhotos(newText)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private val infiniteScrollListener by lazy {
        InfiniteScrollListener(
            adapter,
            recyclerView.layoutManager as StaggeredGridLayoutManager,
            presenter::nextPage
        )
    }

    class InfiniteScrollListener(
        private val adapter: RecyclerView.Adapter<*>,
        private val layoutManager: StaggeredGridLayoutManager,
        private val func: () -> Unit
    ) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = recyclerView.childCount
            val totalItemCount = adapter.itemCount
            var firstVisibleItem: IntArray? = null
            firstVisibleItem =
                layoutManager.findFirstCompletelyVisibleItemPositions(firstVisibleItem)

            var firstItemNumber = 0
            firstVisibleItem?.let {
                if (it.isNotEmpty()) {
                    firstItemNumber = it[0]
                }

                if ((firstItemNumber + visibleItemCount) >= totalItemCount - 10) {
                    func()
                }
            }
        }
    }
}

object MainBlurData {
    lateinit var bitmap: Bitmap

    fun recycle() {
        if (::bitmap.isInitialized) {
            bitmap.recycle()
        }
    }
}