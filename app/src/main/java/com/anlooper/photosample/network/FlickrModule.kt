package com.anlooper.photosample.network

import com.anlooper.photosample.data.PhotoResponse
import com.anlooper.photosample.network.retorfit.createRetrofit
import rx.Observable

/**
 * Created by Tae-hwan on 7/22/16.
 */
class FlickrModule {

    val FLICKER_URL: String = "https://api.flickr.com/services/rest/"

    private val photoApi: FlickrServiceInterface

    init {
        photoApi = createRetrofit(FlickrServiceInterface::class.java, FLICKER_URL)
    }

    /**
     * get Recent photo list
     */
    fun getRecentPhotos(page: Int):
            Observable<PhotoResponse> = photoApi.getFlickrRecentPhotos(page)

    /**
     * Search photo list
     */
    fun getSearchPhotos(page: Int, safeSearch: Int, text: String):
            Observable<PhotoResponse> = photoApi.getFlickrPhotosSearch(page, safeSearch, text)
}