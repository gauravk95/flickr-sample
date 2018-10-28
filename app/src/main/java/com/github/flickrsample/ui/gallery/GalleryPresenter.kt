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
package com.github.flickrsample.ui.gallery

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
 * Gallery Presenter where most of the logic stuff happens
 * Extends functionality of [BasePresenter]
 * Implements Screen specific Presenter tasks [GalleryContract.Presenter]
 *
 * Created by gk
 */
class GalleryPresenter @Inject
constructor(appRepository: AppRepository,
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable) :
        BasePresenter<GalleryContract.View>(appRepository, schedulerProvider, compositeDisposable),
        GalleryContract.Presenter {

    private var mDisposable: Disposable? = null

    //indicates whether it last page
    var isLastPage = false

    //indicates whether the items is loading elements or not
    var isLoading = false

    //holds the list of all the photos loaded
    var mPhotoList: MutableList<PhotoItem> = mutableListOf()

    //holds the current page
    var page: Int = 1

    //holds the element per page
    private var perPage: Int = FlickrUtils.DEFAULT_PAGE_SIZE

    private var query: String = FlickrUtils.DEFAULT_QUERY

    /**
     * Load the photos list from the data source and update the display for the first time
     * Uses Pagination indicated by [page] and limit [perPage]
     */
    override fun loadFirstPhotos(refresh: Boolean, key: String?, query: String) {

        //key is required for the call
        if (key.isNullOrEmpty()) {
            view?.showSnackBarMessage(R.string.default_error_message)
            return
        }

        isLoading = true
        this.query = query

        view?.showProgressDialog()

        if (refresh)
            dataSource.refreshItems()

        //remove the previous disposable from composite disposable, for multiple load items calls
        if (mDisposable != null)
            compositeDisposable.delete(mDisposable!!)

        mDisposable = dataSource.getPhotoItemList(key!!, query, page, perPage)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ photoItems: List<PhotoItem> ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.dismissProgressDialog()

                    if (photoItems.isNotEmpty()) {
                        mPhotoList.clear()
                        mPhotoList.addAll(photoItems)
                        view?.initItemList(mPhotoList)
                    }else
                        view?.showEmptyListUI()
                }, { throwable: Throwable? ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.dismissProgressDialog()
                    handleApiError(throwable)
                })

        compositeDisposable.add(mDisposable!!)
    }

    /**
     * Load the photos list from the data source and update the display
     * Uses Pagination indicated by [page] and limit [perPage]
     */
    override fun loadNextPhotos(refresh: Boolean, key: String?, query: String) {

        //key is required for the call
        if (key.isNullOrEmpty()) {
            view?.showSnackBarMessage(R.string.default_error_message)
            return
        }

        //end point reached no need to get more data
        if (dataSource.getPaginationStatus()) {
            isLastPage = true
            return
        }

        this.query = query

        //increment the page number
        page++

        isLoading = true

        view?.showBottomLoading()

        if (refresh)
            dataSource.refreshItems()

        //remove the previous disposable from composite disposable, for multiple load items calls
        if (mDisposable != null)
            compositeDisposable.delete(mDisposable!!)

        mDisposable = dataSource.getPhotoItemList(key!!, query, page, perPage)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ photoItems: List<PhotoItem> ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.hideBottomLoading()

                    if (photoItems.isNotEmpty()) {
                        mPhotoList.addAll(photoItems)
                        view?.refreshItemList()
                    }

                }, { throwable: Throwable? ->
                    if (!isViewAttached)
                        return@subscribe

                    isLoading = false

                    view?.hideBottomLoading()
                    handleApiError(throwable)
                })

        compositeDisposable.add(mDisposable!!)
    }

    /**
     * Handle image click event
     */
    override fun onImageClicked(position: Int) {
        if (position < mPhotoList.size)
            view?.launchImageViewActivity(mPhotoList[position].id, query)
    }
}
