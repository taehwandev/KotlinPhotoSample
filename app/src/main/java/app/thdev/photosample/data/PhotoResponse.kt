package app.thdev.photosample.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Tae-hwan on 7/22/16.
 */
data class PhotoResponse(
    @SerializedName("photos") val photos: PhotoPageInfo
)

data class PhotoPageInfo(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("photo") val photo: List<PhotoItem>,
    @SerializedName("stat") val stat: String
)

@Parcelize
data class PhotoItem(
    @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Long,
    @SerializedName("title") val title: String,
    @SerializedName("ispublic") val isPublic: Long,
    @SerializedName("isfriend") val isFriend: Long,
    @SerializedName("isfamily") val isFamily: Long
) : Parcelable {

    fun getImageUrl(): String {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", farm, server, id, secret)
    }
}