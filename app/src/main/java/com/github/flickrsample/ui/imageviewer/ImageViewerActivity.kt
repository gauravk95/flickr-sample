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

import android.os.Bundle

import com.github.flickrsample.R
import com.github.flickrsample.base.BaseActivity
import com.github.flickrsample.utils.AppConstants
import com.github.flickrsample.utils.ext.findFragmentById
import com.github.flickrsample.utils.ext.goFullscreen
import com.github.flickrsample.utils.ext.setFragment

/**
 * The ImageViewer activity, holds the [ImageViewerFragment]
 *
 * Created by gk
 */
class ImageViewerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        val photoId = intent.getStringExtra(AppConstants.INTENT_ARGS_PHOTO_ID)
        val query = intent.getStringExtra(AppConstants.INTENT_ARGS_QUERY)

        val imageViewerFragment = findFragmentById<ImageViewerFragment>(R.id.content_frame)
        if (imageViewerFragment == null) {
            setFragment(ImageViewerFragment.newInstance(photoId, query),R.id.content_frame)
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) goFullscreen()
    }

}
