package com.anlooper.photosample.adapter

import android.content.Context
import android.view.ViewGroup
import com.anlooper.photosample.adapter.contract.PhotoAdapterContract
import com.anlooper.photosample.adapter.view.PhotoViewHolder
import com.anlooper.photosample.data.Photo
import com.anlooper.photosample.listener.OnClickListener
import com.anlooper.photosample.listener.OnLongClickListener

/**
 * Created by Tae-hwan on 7/22/16.
 */
class PhotoAdapter(context: Context) : BaseRecyclerAdapter<Photo>(context), PhotoAdapterContract.View, PhotoAdapterContract.Model {

    var longClickListener: OnLongClickListener? = null
        private set

    // fun onLongClickListener(baseRecyclerAdapter: BaseRecyclerAdapter<*>, position: Int): Boolean
    fun setLongClickListener(listener: (BaseRecyclerAdapter<*>, Int) -> Boolean) {
        this.longClickListener = object : OnLongClickListener {
            override fun onLongClickListener(baseRecyclerAdapter: BaseRecyclerAdapter<*>, position: Int)
                    = listener(baseRecyclerAdapter, position)
        }
    }

    var onClickListener: OnClickListener? = null
        private set

    fun setOnClickListener(listener: (BaseRecyclerAdapter<*>, Int) -> Unit, fn: (() -> Unit)? = null) {
        this.onClickListener = object : OnClickListener {
            override fun OnClickListener(baseRecyclerAdapter: BaseRecyclerAdapter<*>, position: Int) {
                listener(baseRecyclerAdapter, position)
            }

            override fun test() {
                if (fn != null) {
                    fn()
                }
            }
        }
    }

    override fun refresh() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<Photo>
            = PhotoViewHolder(parent, this)
}