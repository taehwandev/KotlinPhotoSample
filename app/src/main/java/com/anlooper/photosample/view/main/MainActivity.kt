package com.anlooper.photosample.view.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import com.anlooper.photosample.R
import tech.thdev.base.util.replaceContentFragment
import tech.thdev.base.view.BaseActivity

class MainActivity : BaseActivity() {

    private val toolbar by lazy {
        findViewById(R.id.toolbar) as Toolbar
    }

    private val fab by lazy {
        findViewById(R.id.fab) as FloatingActionButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val fragment: MainFragment = MainFragment()
        replaceContentFragment(R.id.frame_layout, fragment)

        fab.setOnClickListener { Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }
}
