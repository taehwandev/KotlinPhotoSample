package com.anlooper.photosample.listener

import com.anlooper.photosample.adapter.BaseRecyclerAdapter


/**
 * Created by Tae-hwan on 7/29/16.
 */
interface OnLongClickListener {

    fun onLongClickListener(baseRecyclerAdapter: BaseRecyclerAdapter<*>, position: Int): Boolean
}