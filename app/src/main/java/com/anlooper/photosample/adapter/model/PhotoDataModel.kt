package com.anlooper.photosample.adapter.model

import com.anlooper.photosample.data.Photo

/**
 * Created by Tae-hwan on 7/22/16.
 */
interface PhotoDataModel {

    fun getItem(position: Int): Photo?

    fun addItem(item: Photo)

    fun clean()
}