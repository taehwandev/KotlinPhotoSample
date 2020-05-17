package app.thdev.photosample.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import app.thdev.photosample.R
import app.thdev.photosample.data.PhotoItem
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val KEY_PHOTO_DATA = "key-photo_data"

        fun newIntent(context: Context, photoItem: PhotoItem): Intent =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(KEY_PHOTO_DATA, photoItem)
            }
    }

    private val image by lazy {
        findViewById<ImageView>(R.id.img)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        val photo: PhotoItem? = intent.getParcelableExtra<PhotoItem>(KEY_PHOTO_DATA)
        Glide.with(this)
            .load(photo?.getImageUrl())
            .centerCrop()
            .into(image)
    }
}
