package com.anlooper.photosample.data

import android.os.Parcel
import android.os.Parcelable
import tech.thdev.base.model.BaseItem

/**
 * Created by Tae-hwan on 7/22/16.
 */
data class Photo(val id: String,
                 val owner: String,
                 val secret: String,
                 val server: String,
                 val farm: Long,
                 val title: String,
                 val ispublic: Long,
                 val isfriend: Long,
                 val isfamily: Long,
                 override val viewType: Int) : BaseItem, Parcelable {

    fun getImageUrl(): String {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", farm, server, id, secret)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Photo> = object : Parcelable.Creator<Photo> {
            override fun createFromParcel(source: Parcel): Photo = Photo(source)
            override fun newArray(size: Int): Array<Photo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong(),
            source.readString(),
            source.readLong(),
            source.readLong(),
            source.readLong(),
            source.readInt())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(owner)
        dest?.writeString(secret)
        dest?.writeString(server)
        dest?.writeLong(farm)
        dest?.writeString(title)
        dest?.writeLong(ispublic)
        dest?.writeLong(isfriend)
        dest?.writeLong(isfamily)
        dest?.writeInt(viewType)
    }
}