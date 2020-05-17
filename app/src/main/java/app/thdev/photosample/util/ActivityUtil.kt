package app.thdev.photosample.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replaceContentFragment(@IdRes frameId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(frameId, fragment)
        .commitAllowingStateLoss()
}

fun AppCompatActivity.addFragment(@IdRes frameId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .add(frameId, fragment)
        .commitAllowingStateLoss()
}
