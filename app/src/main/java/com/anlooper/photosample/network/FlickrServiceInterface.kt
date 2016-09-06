package com.anlooper.photosample.network

import com.anlooper.photosample.BuildConfig
import com.anlooper.photosample.data.PhotoResponse
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Tae-hwan on 7/22/16.
 */
interface FlickrServiceInterface {

    @POST("?method=flickr.interestingness.getList&format=json&nojsoncallback=1&api_key=" + BuildConfig.FLICKR_API_KEY)
    fun getFlickrRecentPhotos(
            @Query("page") page: Int): Observable<PhotoResponse>

    @POST("?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=" + BuildConfig.FLICKR_API_KEY)
    fun getFlickrPhotosSearch(
            @Query("page") page: Int,
            @Query("safe_search") safeSearch: Int,
            @Query("text") text: String): Observable<PhotoResponse>
}
