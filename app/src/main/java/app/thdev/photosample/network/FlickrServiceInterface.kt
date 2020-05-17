package app.thdev.photosample.network

import app.thdev.photosample.BuildConfig
import app.thdev.photosample.data.PhotoResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Tae-hwan on 7/22/16.
 */
interface FlickrServiceInterface {

    @POST("?method=flickr.interestingness.getList&format=json&nojsoncallback=1&api_key=${BuildConfig.FLICKR_API_KEY}")
    fun getFlickrRecentPhotos(
        @Query("page") page: Int
    ): Flowable<PhotoResponse>

    @POST("?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=${BuildConfig.FLICKR_API_KEY}")
    fun getFlickrPhotosSearch(
        @Query("page") page: Int,
        @Query("safe_search") safeSearch: Int,
        @Query("text") text: String
    ): Flowable<PhotoResponse>
}
