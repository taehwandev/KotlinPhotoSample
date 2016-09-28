package com.anlooper.photosample.adapter

import tech.thdev.base.model.BaseItem

/**
 * Created by taehwan on 2016. 9. 28..
 */
interface BaseRecyclerModel<ITEM: BaseItem> {

    fun addItem(item: ITEM)

    fun addItem(position: Int, item: ITEM)

    fun addItems(items: List<ITEM>)

    fun clear()

    fun removeItem(item: ITEM)

    fun removeItem(position: Int)

    fun getItem(position: Int): ITEM?

    fun getCount(): Int
}