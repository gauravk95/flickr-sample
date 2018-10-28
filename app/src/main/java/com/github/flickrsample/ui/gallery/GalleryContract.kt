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

import com.github.flickrsample.base.BaseContract
import com.github.flickrsample.data.models.local.PhotoItem

/**
 * The gallery contract, consists of Screen specific Presenter and View interface
 *
 * Created by gk
 */
interface GalleryContract {

    interface View : BaseContract.View<Presenter> {

        fun initItemList(photoItemList: List<PhotoItem>)

        fun refreshItemList()

        fun showEmptyListUI()

        fun showBottomLoading()

        fun hideBottomLoading()

        fun launchImageViewActivity(photoId: String, query: String)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun loadFirstPhotos(refresh: Boolean,
                            key: String?,
                            query: String = "")

        fun loadNextPhotos(refresh: Boolean,
                           key: String?,
                           query: String = "")

        fun onImageClicked(position: Int)

    }
}
