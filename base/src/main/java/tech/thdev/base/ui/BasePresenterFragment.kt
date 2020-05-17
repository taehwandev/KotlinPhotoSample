package tech.thdev.base.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import tech.thdev.base.presenter.BasePresenter
import tech.thdev.base.presenter.BaseView

abstract class BasePresenterFragment<in VIEW : BaseView, PRESENTER : BasePresenter<VIEW>> :
    Fragment(), BaseView {

    protected lateinit var presenter: PRESENTER
        private set

    abstract fun onCreatePresenter(): PRESENTER

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = onCreatePresenter()
        presenter.attachView(this as VIEW)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}