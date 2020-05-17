package app.thdev.photosample.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import app.thdev.photosample.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainBlurActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_IMAGE_URL = "image_url"

        fun newIntent(context: Context, imageUrl: String): Intent =
            Intent(context, MainBlurActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URL, imageUrl)
            }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_blur_activity)

        findViewById<AppCompatImageView>(R.id.img_blur_background).setImageBitmap(MainBlurData.bitmap)

        intent.getStringExtra(EXTRA_IMAGE_URL)?.let {
            Glide.with(this)
                .load(it)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        finish()

                        Toast.makeText(this@MainBlurActivity, "Image load fail", Toast.LENGTH_SHORT)
                            .show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(findViewById<AppCompatImageView>(R.id.img_view))
        }

        findViewById<View>(R.id.container).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    finish()
                }
            }
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MainBlurData.recycle()
    }
}