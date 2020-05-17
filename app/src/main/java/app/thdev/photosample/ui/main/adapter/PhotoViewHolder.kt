package app.thdev.photosample.ui.main.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GestureDetectorCompat
import app.thdev.photosample.R
import app.thdev.photosample.data.PhotoItem
import app.thdev.photosample.ui.main.CustomGestureDetector
import com.bumptech.glide.Glide
import tech.thdev.simpleadapter.holder.BaseViewHolder

/**
 * Created by Tae-hwan on 7/22/16.
 */
@SuppressLint("ClickableViewAccessibility")
class PhotoViewHolder(
    parent: ViewGroup,
    private val onClick: (adapterPosition: Int) -> Unit,
    private val onLongClick: (adapterPosition: Int) -> Unit
) : BaseViewHolder<PhotoItem>(R.layout.main_photo_item, parent) {

    private val imageView by lazy {
        itemView.findViewById(R.id.image_view) as ImageView
    }

    private val gestureDetector = GestureDetectorCompat(itemView.context,
        CustomGestureDetector(
            onClickEvent = {
                onClick(adapterPosition)
            },
            onLongClick = {
                itemView.clearFocus()
                onLongClick(adapterPosition)
            }
        ))

    init {
        itemView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun onBindViewHolder(item: PhotoItem) {
        Glide.with(imageView.context)
            .load(item.getImageUrl())
            .placeholder(android.R.drawable.ic_menu_gallery)
            .centerCrop()
            .into(imageView)
    }
}