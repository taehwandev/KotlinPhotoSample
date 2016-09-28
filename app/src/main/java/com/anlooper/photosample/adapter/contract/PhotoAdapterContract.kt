package com.anlooper.photosample.adapter.contract

import com.anlooper.photosample.adapter.BaseRecyclerModel
import com.anlooper.photosample.data.Photo

/**
 * Created by Tae-hwan on 7/22/16.
 */
interface PhotoAdapterContract {

    interface View {
        fun refresh()
    }

    interface Model : BaseRecyclerModel<Photo> {

    }
}