package com.anlooper.photosample.view.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.anlooper.photosample.R
import com.anlooper.photosample.adapter.PhotoAdapter
import com.anlooper.photosample.constant.Constant
import com.anlooper.photosample.constant.FlickrTypeConstant
import com.anlooper.photosample.network.FlickrModule
import com.anlooper.photosample.util.createBlurImage
import com.anlooper.photosample.view.detail.DetailActivity
import com.anlooper.photosample.view.main.presenter.MainContract
import com.anlooper.photosample.view.main.presenter.MainPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import tech.thdev.base.view.BasePresenterFragment
import java.util.concurrent.TimeUnit

/**
 * Created by Tae-hwan on 7/21/16.
 */
class MainFragment : BasePresenterFragment<MainContract.View, MainContract.Presenter>(), MainContract.View {

    private var loading: Boolean = false
    private var page: Int = 0

    private var adapter: PhotoAdapter? = null
    private lateinit var recyclerView: RecyclerView

    private var clBlur: ConstraintLayout? = null
    private var imgBlurBackground: ImageView? = null
    private var imgView: ImageView? = null

    private var containerMain: CoordinatorLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePresenter() = MainPresenter(FlickrModule())

    override fun getLayout() = R.layout.fragment_main

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PhotoAdapter(context)
        adapter?.setLongClickListener { baseRecyclerAdapter, i ->
            presenter?.updateLongClickItem(i) ?: false
        }

        adapter?.setOnClickListener { baseRecyclerAdapter, i ->
            presenter?.loadDetailView(i)
        }

        recyclerView = view?.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.addOnScrollListener(InfiniteScrollListener({ presenter!!.loadPhotos(page) },
                recyclerView.layoutManager as StaggeredGridLayoutManager))
        recyclerView.adapter = adapter

        presenter?.setDataModel(adapter)
        initPhotoList()

        clBlur = activity?.findViewById(R.id.constraintLayout) as ConstraintLayout
        imgBlurBackground = activity?.findViewById(R.id.img_blur_background) as ImageView
        imgView = activity?.findViewById(R.id.img_view) as ImageView

        containerMain = activity?.findViewById(R.id.container_main) as CoordinatorLayout
    }

    override fun showDetailView(imageUrl: String?) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(Constant.KEY_IMAGE_URL, imageUrl)
        startActivity(intent)
    }

    override fun showBlurDialog(imageUrl: String?) {
        clBlur?.visibility = View.VISIBLE
        imgView?.visibility = View.VISIBLE
        imgBlurBackground?.visibility = View.VISIBLE

        drawBackgroundImage()

        Glide.with(context)
                .load(imageUrl)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        /*
                         * Timer
                         */
                        Observable.timer(5, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    clBlur?.visibility = View.GONE
                                    imgView?.visibility = View.GONE
                                    imgBlurBackground?.visibility = View.GONE
                                }
                        return false
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        clBlur?.visibility = View.GONE
                        imgView?.visibility = View.GONE
                        imgBlurBackground?.visibility = View.GONE

                        Toast.makeText(context, "Image load fail", Toast.LENGTH_SHORT).show()
                        return false
                    }
                })
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .crossFade()
                .into(imgView)
    }

    /**
     * Root capture...
     */
    private fun drawBackgroundImage() {
        containerMain?.isDrawingCacheEnabled = true
        containerMain?.buildDrawingCache(true)
        val bitmap: Bitmap? = containerMain?.drawingCache
        bitmap?.createBlurImage(context)?.let {
            imgBlurBackground?.setImageBitmap(it)
        }

        containerMain?.isDrawingCacheEnabled = false
    }

    override fun showProgress() {
        loading = true
    }

    override fun hideProgress() {
        loading = false
    }

    override fun showFailLoad() {
        Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show()
    }

    override fun refresh() {
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.unSubscribeSearch()
        recyclerView.removeOnScrollListener(InfiniteScrollListener({
                presenter?.loadPhotos(page)
            }, recyclerView.layoutManager as StaggeredGridLayoutManager))
    }

    override fun initPhotoList() {
        page = 0
        presenter?.loadPhotos(page)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)

        val searchView: SearchView = MenuItemCompat.getActionView(menu?.findItem(R.id.action_search)) as SearchView
        val searchManager: SearchManager? = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        page = 0
                        presenter?.searchPhotos(page, FlickrTypeConstant.TYPE_SAFE_SEARCH_SAFE, query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        page = 0
                        presenter?.searchPhotos(page, FlickrTypeConstant.TYPE_SAFE_SEARCH_SAFE, newText)
                        return true
                    }
                }
        )

        super.onCreateOptionsMenu(menu, inflater)
    }

    inner class InfiniteScrollListener(
            val func: () -> Unit,
            val layoutManager: StaggeredGridLayoutManager) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = recyclerView?.childCount as Int
            val totalItemCount = adapter?.itemCount as Int
            var firstVisibleItem: IntArray? = null
            firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPositions(firstVisibleItem)

            var firstItemNumber = 0
            firstVisibleItem?.let {
                if (it.size > 0) {
                    firstItemNumber = it[0]
                }

                if (!loading && (firstItemNumber + visibleItemCount) >= totalItemCount - 10) {
                    ++page
                    func()
                }
            }
        }
    }
}
