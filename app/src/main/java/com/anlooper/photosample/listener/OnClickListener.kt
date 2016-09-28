package com.anlooper.photosample.listener

import com.anlooper.photosample.adapter.BaseRecyclerAdapter


/**
 * Created by Tae-hwan on 8/1/16.
 */
interface OnClickListener {

    fun OnClickListener(baseRecyclerAdapter: BaseRecyclerAdapter<*>, position: Int)

    fun test()
}