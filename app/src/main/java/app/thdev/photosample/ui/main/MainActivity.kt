package app.thdev.photosample.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import app.thdev.photosample.R
import app.thdev.photosample.util.addFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }

    private val fab by lazy {
        findViewById<FloatingActionButton>(R.id.fab)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val fragment = MainFragment()
            addFragment(R.id.frame_layout, fragment)
        }

        fab.setOnClickListener {
            Snackbar.make(
                it,
                "Replace with your own action",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
        }
    }
}
