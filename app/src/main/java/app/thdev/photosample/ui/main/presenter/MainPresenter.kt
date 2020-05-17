package app.thdev.photosample.ui.main.presenter

import app.thdev.photosample.constant.TYPE_SAFE_SEARCH_SAFE
import app.thdev.photosample.data.PhotoItem
import app.thdev.photosample.data.PhotoResponse
import app.thdev.photosample.network.FlickrModule
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import tech.thdev.base.presenter.CommonPresenter
import tech.thdev.simpleadapter.data.source.AdapterRepository
import java.util.concurrent.TimeUnit

/**
 * Created by Tae-hwan on 7/21/16.
 */
class MainPresenter(
    private val retrofitFlicker: FlickrModule,
    private val adapterRepository: AdapterRepository
) : CommonPresenter<MainContract.View>(), MainContract.Presenter {

    private var isLoading: Boolean = false
    private var page: Int = 0

    private val searchSubject: PublishSubject<String> = PublishSubject.create()
    private var searchSubscription: Disposable? = null

    init {
        initSubscription()
    }

    override fun init() {
        page = 0
        val photos = retrofitFlicker.getRecentPhotos(page)
        subscribePhoto(photos)
    }

    private fun initSubscription() {
        searchSubscription = searchSubject
            .throttleWithTimeout(300, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe(
                { loadSearchPhotos(it) },
                { view?.showFailLoad() })
    }

    override fun searchPhotos(text: String?) {
        if (searchSubscription?.isDisposed != true) {
            searchSubscription?.dispose()
        }

        adapterRepository.clear()

        initSubscription()

        text?.let {
            searchSubject.onNext(it)
        } ?: init()
    }

    private fun loadSearchPhotos(query: String?) {
        val photos = retrofitFlicker.getSearchPhotos(page, TYPE_SAFE_SEARCH_SAFE, query ?: "")
        adapterRepository.clear()
        subscribePhoto(photos)
    }

    private fun subscribePhoto(photos: Flowable<PhotoResponse>) {
        photos
            .subscribeOn(Schedulers.io())
            .map { it.photos }
            .filter { it != null && it.photo.isNotEmpty() }
            .filter { it != null && it.page != it.pages }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isLoading = true
                view?.showProgress()
            }
            .doOnComplete {
                isLoading = false
                view?.reloadAdapter()
                view?.hideProgress()
            }
            .subscribe(
                {
                    adapterRepository.addItems(0, it.photo)
                },
                {
                    view?.showFailLoad()
                })
    }

    override fun onLongClick(adapterPosition: Int) {
        val photo = adapterRepository.getItem(adapterPosition) as PhotoItem
        view?.showBlurDialog(photo.getImageUrl())
    }

    override fun onClickItem(adapterPosition: Int) {
        val photo = adapterRepository.getItem(adapterPosition) as PhotoItem
        view?.showDetailView(photo)
    }

    override fun nextPage() {

    }

    override fun destroy() {
        page = 0
        isLoading = false
        searchSubscription?.dispose()
    }
}