package app.thdev.photosample.ui.main.presenter

import app.thdev.photosample.data.PhotoItem
import tech.thdev.base.presenter.BasePresenter
import tech.thdev.base.presenter.BaseView

/**
 * Created by Tae-hwan on 7/21/16.
 */
interface MainContract {

    interface View : BaseView {

        fun showFailLoad()

        fun showProgress()

        fun hideProgress()

        fun reloadAdapter()

        fun showBlurDialog(imageUrl: String)

        fun showDetailView(photo: PhotoItem)

    }

    interface Presenter : BasePresenter<View> {

        fun init()

        fun onClickItem(adapterPosition: Int)

        /**
         * Long click item
         */
        fun onLongClick(adapterPosition: Int)

        /**
         * keyword search photos
         */
        fun searchPhotos(text: String?)

        /**
         * 다음 페이지 정보를 불러온다
         */
        fun nextPage()

        /**
         * destroy.
         */
        fun destroy()
    }
}