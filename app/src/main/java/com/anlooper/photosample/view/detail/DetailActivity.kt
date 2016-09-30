package com.anlooper.photosample.view.detail

import android.os.Bundle
import android.widget.ImageView
import com.anlooper.photosample.R
import com.anlooper.photosample.constant.Constant
import com.anlooper.photosample.data.Photo
import com.bumptech.glide.Glide
import tech.thdev.base.view.BaseActivity

class DetailActivity : BaseActivity() {

    val image by lazy {
        findViewById(R.id.img) as ImageView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val photo: Photo? = intent.getParcelableExtra<Photo>(Constant.KEY_PHOTO_DATA)
        Glide.with(this)
                .load(photo?.getImageUrl())
                .centerCrop()
                .into(image)
    }
}
