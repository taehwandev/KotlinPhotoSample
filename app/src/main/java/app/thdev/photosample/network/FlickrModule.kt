package app.thdev.photosample.network

import app.thdev.photosample.data.PhotoResponse
import app.thdev.photosample.network.retorfit.createRetrofit
import io.reactivex.rxjava3.core.Flowable

/**
 * Created by Tae-hwan on 7/22/16.
 */
class FlickrModule {

    companion object {
        private const val FLICKER_URL: String = "https://api.flickr.com/services/rest/"
    }

    private val photoApi: FlickrServiceInterface by lazy {
        createRetrofit(FlickrServiceInterface::class.java, FLICKER_URL)
    }

    /**
     * get Recent photo list
     */
    fun getRecentPhotos(page: Int): Flowable<PhotoResponse> =
        photoApi.getFlickrRecentPhotos(page)

    /**
     * Search photo list
     */
    fun getSearchPhotos(page: Int, safeSearch: Int, text: String): Flowable<PhotoResponse> =
        photoApi.getFlickrPhotosSearch(page, safeSearch, text)
}