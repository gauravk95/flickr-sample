/*
    Copyright 2018 Gaurav Kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.github.flickrsample.ui.imageviewer

import com.github.flickrsample.R
import com.github.flickrsample.base.BasePresenter
import com.github.flickrsample.data.models.local.PhotoItem
import com.github.flickrsample.data.source.repository.AppRepository
import com.github.flickrsample.utils.FlickrUtils
import com.github.flickrsample.utils.rx.SchedulerProvider

import javax.inject.Inject

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * ImageViewer Presenter where most of the logic stuff happens
 * Extends functionality of [BasePresenter]
 * Implements Screen specific Presenter tasks [ImageViewerContract.Presenter]
 *
 * Created by gk
 */
class ImageViewerPresenter @Inject
constructor(appRepository: AppRepository,
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable) :
        BasePresenter<ImageViewerContract.View>(appRepository, schedulerProvider, compositeDisposable),
        ImageViewerContract.Presenter {

    private var mDisposable: Disposable? = null

    //holds the query, that is used for search
    var query: String? = null

    //holds the list of all the photos loaded
    var mPhotoList: MutableList<PhotoItem> = mutableListOf()

    //indicates whether the items is loading elements or not
    var isLoading = false

    //holds the current page
    var page: Int = 1

    //holds the element per page
    private var perPage: Int = FlickrUtils.DEFAULT_PAGE_SIZE

    /**
     * Initialize parameters
     */
    override fun init() {
        page = dataSource.getPageNumber()
    }

    /**
     * Load the Photos Items and shows them in sliders, Sets the current page to one identified by photoId
     */
    override fun loadPhotoFromPhotoId(key: String?, query: String?, photoId: String?) {

        //key is required for the call
        if (key.isNullOrEmpty() || query.isNullOrEmpty()) {
            view?.showSnackBarMessage(R.string.default_error_message)
            return
        }

        this.query = query

        view?.showProgressDialog()

        //remove the previous disposable from composite disposable, for multiple load items calls
        if (mDisposable != null)
            compositeDisposable.delete(mDisposable!!)

        mDisposable = dataSource.getCachedPhotoItems()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ photoItems: List<PhotoItem> ->
                    if (!isViewAttached)
                        return@subscribe

                    view?.dismissProgressDialog()

                    if (photoItems.isNotEmpty()) {
                        mPhotoList.clear()
                        mPhotoList.addAll(photoItems)
                        view?.initViewPager(mPhotoList)
                        view?.updateCurrentPage(findPhotoItemIndexById(photoId, mPhotoList))
                    } else
                        view?.showEmptyUI()
                }, { throwable: Throwable? ->
                    if (!isViewAttached)
                        return@subscribe

                    view?.dismissProgressDialog()
                    handleApiError(throwable)
                })

        compositeDisposable.add(mDisposable!!)
    }

    /**
     * Get New Photos if not end is reached
     */
    override fun loadNewPhotos(key: String?, position: Int) {
        // no need to get new data if pagination is ended,
        // or view pager end is not reached
        if (position < (mPhotoList.size - 1) || dataSource.getPaginationStatus() || isLoading)
            return

        if (key.isNullOrEmpty()) {
            view?.showSnackBarMessage(R.string.default_error_message)
            return
        }

        //increment the page number
        page++

        isLoading = true

        view?.showProgressBar()

        //remove the previous disposable from composite disposable, for multiple load items calls
        if (mDisposable != null)
            compositeDisposable.delete(mDisposable!!)

        mDisposable = dataSource.getPhotoItemList(key!!, query!!, page, perPage)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ photoItems: List<PhotoItem> ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.hideProgressBar()

                    if (photoItems.isNotEmpty()) {
                        mPhotoList.addAll(photoItems)
                        view?.refreshViewPager()
                    }

                }, { throwable: Throwable? ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.hideProgressBar()
                    handleApiError(throwable)
                })

        compositeDisposable.add(mDisposable!!)
    }

    /**
     * Finds the element and its associated Index
     */
    private fun findPhotoItemIndexById(photoId: String?, photoItems: List<PhotoItem>): Int {
        for (i in photoItems.indices) {
            if (photoItems[i].id == photoId)
                return i
        }
        return -1
    }
}
