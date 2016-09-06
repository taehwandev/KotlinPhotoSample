package com.anlooper.photosample.view.main.presenter

import android.text.TextUtils
import com.anlooper.photosample.data.PhotoResponse
import com.anlooper.photosample.data.SearchData
import com.anlooper.photosample.network.FlickrModule
import com.anlooper.photosample.adapter.model.PhotoDataModel
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import tech.thdev.base.presenter.AbstractPresenter
import java.util.concurrent.TimeUnit

/**
 * Created by Tae-hwan on 7/21/16.
 */
class MainPresenter(val retrofitFlicker: FlickrModule) : AbstractPresenter<MainContract.View>(), MainContract.Presenter {

    private var model: PhotoDataModel? = null

    private val searchSubject: PublishSubject<SearchData>
    private var searchSubscription: Subscription? = null

    init {
        searchSubject = PublishSubject.create()
        initSubscription()
    }

    private fun initSubscription() {
        searchSubscription = searchSubject
                .onBackpressureBuffer()
                .throttleWithTimeout(200, TimeUnit.MICROSECONDS)
                .observeOn(Schedulers.io())
                .subscribe(
                        { loadSearchPhotos(it) },
                        { view?.showFailLoad() })
    }

    override fun setDataModel(model: PhotoDataModel?) {
        this.model = model
    }

    override fun loadPhotos(page: Int) {
        val photos = retrofitFlicker.getRecentPhotos(page)
        subscribePhoto(photos)
    }

    override fun searchPhotos(page: Int, safeSearch: Int, text: String?) {
        if (!(searchSubscription?.isUnsubscribed as Boolean)) {
            searchSubscription?.unsubscribe()
        }

        model?.clean()

        initSubscription()
        if (!TextUtils.isEmpty(text)) {
            searchSubject.onNext(SearchData(text!!, page, safeSearch))

        } else {
            view?.initPhotoList()
        }
    }

    private fun loadSearchPhotos(searchData: SearchData?) {
        searchData?.let {
            val photos = retrofitFlicker.getSearchPhotos(it.page, it.safeSearch, it.text)
            model?.clean()
            subscribePhoto(photos)
        }
    }

    private fun subscribePhoto(photos: Observable<PhotoResponse>) {
        photos
                .subscribeOn(Schedulers.io())
                .map { it.photos }
                .filter { it != null && it.photo.size > 0 }
                .filter { it != null && it.page != it.pages }
                .flatMap { Observable.from(it.photo) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showProgress()
                }
                .doOnUnsubscribe {
                    view?.refresh()
                    view?.hideProgress()
                }
                .subscribe(
                        { model?.addItem(it) },
                        { view?.showFailLoad() })
    }

    override fun updateLongClickItem(position: Int): Boolean {
        val photo = model?.getItem(position)
        view?.showBlurDialog(photo?.getImageUrl())
        return true
    }

    override fun loadDetailView(position: Int) {
        val photo = model?.getItem(position)
        view?.showDetailView(photo?.getImageUrl())
    }


    override fun unSubscribeSearch() {
        searchSubscription?.unsubscribe()
    }
}