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
package com.github.flickrsample.utils

import android.content.Context
import android.graphics.Color
import android.support.v4.widget.CircularProgressDrawable
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.StringSignature
import com.github.flickrsample.R

/**
 * Utilities for other general stuffs
 *
 * Created by gk
 */

object GeneralUtils {

    fun checkStringNotEmpty(str: String?): Boolean {
        return str != null && str != ""
    }

    /**
     * Loads the image into given image view
     *
     * FIXME: uses disk caching, set max size to avoid using lot of disk space,
     * NOTE : do cache cleanup for unused images
     *
     */
    fun loadImageFromLink(context: Context, pic: ImageView, link: String) {
        if (GeneralUtils.checkStringNotEmpty(link)) {
            Glide.with(context.applicationContext)
                    .load(link)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(pic)
        }
    }

    /**
     * Loads the image into given image view, with progress bar as placeholder
     *
     * FIXME: uses disk caching, set max size to avoid using lot of disk space,
     * NOTE : do cache cleanup for unused images
     *
     */
    fun loadImageFromLinkWithLoadingProgress(context: Context, pic: ImageView, link: String) {
        if (GeneralUtils.checkStringNotEmpty(link)) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 8f
            circularProgressDrawable.centerRadius = 48f
            circularProgressDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN)
            circularProgressDrawable.start()

            Glide.with(context.applicationContext)
                    .load(link)
                    .placeholder(circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(pic)
        }
    }

}
