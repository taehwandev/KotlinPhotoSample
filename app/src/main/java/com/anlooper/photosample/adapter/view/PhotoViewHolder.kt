package com.anlooper.photosample.adapter.view

import android.view.ViewGroup
import android.widget.ImageView
import com.anlooper.photosample.R
import com.anlooper.photosample.adapter.PhotoAdapter
import com.anlooper.photosample.data.Photo
import com.bumptech.glide.Glide
import tech.thdev.base.adapter.BaseRecyclerAdapter
import tech.thdev.base.adapter.BaseRecyclerViewHolder

/**
 * Created by Tae-hwan on 7/22/16.
 */
class PhotoViewHolder(parent: ViewGroup?,
                      adapter: BaseRecyclerAdapter<Photo>) : BaseRecyclerViewHolder<Photo>(R.layout.item_photo, parent, adapter) {

    private val imageView by lazy {
        itemView.findViewById(R.id.image_view) as ImageView
    }

    override fun onViewHolder(item: Photo?, position: Int) {
        Glide.with(context)
                .load(item?.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .crossFade()
                .into(imageView)
        imageView.setImageResource(android.R.drawable.ic_menu_gallery)

        itemView.setOnLongClickListener {
            getAdapter()?.longClickListener?.onLongClickListener(adapter, position) ?: false
        }

        itemView.setOnClickListener {
            getAdapter()?.onClickListener?.OnClickListener(adapter, position)
        }
    }

    private fun getAdapter(): PhotoAdapter? {
        return adapter as? PhotoAdapter
    }
}