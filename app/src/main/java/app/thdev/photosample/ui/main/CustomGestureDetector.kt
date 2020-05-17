package app.thdev.photosample.ui.main

import android.view.GestureDetector
import android.view.MotionEvent

class CustomGestureDetector(
    private val onClickEvent: (() -> Unit)? = null,
    private val onLongClick: (() -> Unit)? = null
) : GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        onClickEvent?.invoke()
        return super.onSingleTapUp(e)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        onLongClick?.invoke()
        super.onLongPress(e)
    }
}