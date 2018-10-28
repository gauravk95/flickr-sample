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
package com.github.flickrsample.data.source.repository

import android.support.annotation.VisibleForTesting

import com.github.flickrsample.data.models.local.PhotoItem
import com.github.flickrsample.data.models.remote.PhotoResult
import com.github.flickrsample.data.source.prefs.PreferencesHelper
import com.github.flickrsample.di.Local
import com.github.flickrsample.di.Remote

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Flowable

/**
 * The central point to communicate to different data sources like DB, SERVER, SHARED PREFS
 *
 * Created by gk
 */

//FIXME: Local DB is not used currently.
//FIXME: Flicker API may give less number of results for a particular page than the per_page parameter, added a temp fix
//FIXME: Flicker API may give redundant results in different pages
//FIXME: Use a Fixed MAX_SIZE for the mCachedPhotoList to avoid OUT_OF_MEMORY error, keep most recent and active items in it
@Singleton
class AppDataRepository @Inject
constructor(@param:Remote private val mRemoteAppDataSource: AppDataSource,
            @param:Local private val mLocalAppDataSource: AppDataSource,
            private val mPreferenceHelper: PreferencesHelper) : AppRepository {

    @VisibleForTesting
    internal var mCachedPhotoItemList: MutableList<PhotoItem> = mutableListOf()

    /**
     * Holds the current page number
     * if 0: indicates no elements loaded
     * if 1 : loaded first page
     * else : partially loaded
     */
    @VisibleForTesting
    internal var mCurrentPageNumber: Int = 0

    /**
     * Holds the max page available
     */
    @VisibleForTesting
    internal var mMaxPageNumber: Int = 0

    /**
     * A temporary variable, used to indicate fake end point for pagination
     * This is required as the current API call doesn't indicate an endpoint
     * as Pagination is unsupported and also to not load new elements after endpoint
     */
    var mPaginationEndPoint: Boolean = false

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    @VisibleForTesting
    internal var mCacheIsDirty = false

    //get the items from the server
    private fun getItemFromServerDB(key: String,
                                    query: String,
                                    page: Int, perPage: Int): Flowable<PhotoResult> {
        return mRemoteAppDataSource
                .getPhotoResult(key, query, page, perPage)
                .doOnNext { photoResult ->
                    val items = photoResult.photo
                    //mLocalAppDataSource.updatePhotoItemList(items)
                    if (page <= 1)
                        mCachedPhotoItemList.clear()
                    if (photoResult.page >= photoResult.pages)
                        mPaginationEndPoint = true
                    mCurrentPageNumber = photoResult.page
                    mMaxPageNumber = photoResult.pages
                    mCachedPhotoItemList.addAll(items)
                    mCacheIsDirty = false
                }
    }

    /**
     * Gets Photo items from data source
     */
    override fun getPhotoResult(key: String,
                                query: String,
                                page: Int, perPage: Int): Flowable<PhotoResult> {
        // NOTE: Add other sources based on conditions
        /* ******************************************
        Eg:
           if(mCacheDirty)
            getItemFromServerDB(key, query, page, perPage)
           else
            getItemFromLocalDB(key, query, page, perPage)
         ***************************************************/
        return getItemFromServerDB(key, query, page, perPage)
    }

    /**
     * Gets the list of photos for current page
     */
    override fun getPhotoItemList(key: String, query: String, page: Int, perPage: Int): Flowable<List<PhotoItem>> {

        //if end of pagination is reached, return empty list
        if(mPaginationEndPoint)
            return Flowable.just(listOf())

        //request page already available
        if (mMaxPageNumber != 0 && (page !in (mCurrentPageNumber + 1)..(mMaxPageNumber))) {

            //calculate the offset from the page and perPage
            val offset = (page - 1) * perPage

            //if items are available in the cache, directly use it, send batch by batch
            if (mCachedPhotoItemList.isNotEmpty()
                    && !mCacheIsDirty
                    && mCachedPhotoItemList.size > offset) {

                //calculate the batch end index
                val endIndex =
                        if ((offset + perPage) < mCachedPhotoItemList.size) {
                            (offset + perPage)
                        } else {
                            mCachedPhotoItemList.size
                        }

                return Flowable.just(mCachedPhotoItemList.subList(offset, endIndex))
            }
        }

        return getPhotoResult(key, query, page, perPage).map { t: PhotoResult -> t.photo }
    }

    /**
     * Gets all the cached elements
     */
    override fun getCachedPhotoItems(): Flowable<List<PhotoItem>> {
        return Flowable.just(mCachedPhotoItemList)
    }

    /**
     * Updates local db with photo items
     */
    override fun updatePhotoItemList(photoItems: List<PhotoItem>) {
        //mLocalAppDataSource.updatePhotoItemList(photoItems)
    }

    /**
     * Gets the status for pagination
     */
    override fun getPaginationStatus(): Boolean {
        return mPaginationEndPoint
    }

    /**
     * Gets current page number
     */
    override fun getPageNumber(): Int {
        return mCurrentPageNumber
    }

    /**
     * Get maximum page available
     */
    override fun getMaxPageNumber(): Int {
        return mMaxPageNumber
    }

    /**
     * Makes the cache dirty
     */
    override fun refreshItems() {
        mCacheIsDirty = true
    }
}
