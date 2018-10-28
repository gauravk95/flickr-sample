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

import com.github.flickrsample.base.BaseContract
import com.github.flickrsample.data.models.local.PhotoItem

/**
 * The ImageViewer contract, consists of Screen specific Presenter and View interface
 *
 * Created by gk
 */
interface ImageViewerContract {

    interface View : BaseContract.View<Presenter> {

        fun initViewPager(photoItemList: List<PhotoItem>)

        fun refreshViewPager()

        fun updateCurrentPage(pageNo: Int)

        fun showEmptyUI()

        fun showProgressBar()

        fun hideProgressBar()

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun init()

        fun loadPhotoFromPhotoId(key: String?, query: String?, photoId: String?)

        fun loadNewPhotos(key: String?, position: Int)

    }
}
