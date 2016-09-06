package com.anlooper.photosample.view.main.presenter

import com.anlooper.photosample.adapter.model.PhotoDataModel
import tech.thdev.base.presenter.BasePresenter
import tech.thdev.base.presenter.BaseView

/**
 * Created by Tae-hwan on 7/21/16.
 */
interface MainContract {

    interface View : BaseView {

        fun showFailLoad()

        fun refresh()

        fun showProgress()

        fun hideProgress()

        fun initPhotoList()

        fun showBlurDialog(imageUrl: String?)

        fun showDetailView(imageUrl: String?)
    }

    interface Presenter : BasePresenter<View> {

        /**
         * Adapter setting.
         */
        fun setDataModel(model: PhotoDataModel?)

        /**
         * Recent Photos
         */
        fun loadPhotos(page: Int)

        /**
         * keyword search photos
         */
        fun searchPhotos(page: Int, safeSearch: Int, text: String?)

        /**
         * Long click item
         */
        fun updateLongClickItem(position: Int): Boolean

        /**
         * Subscribe destroy.
         */
        fun unSubscribeSearch()

        /**
         * Load Detail Activity
         */
        fun loadDetailView(position: Int)
    }
}