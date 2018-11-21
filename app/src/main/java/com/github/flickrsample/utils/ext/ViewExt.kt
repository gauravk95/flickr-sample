package com.github.flickrsample.utils.ext

import android.graphics.Color
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.flickrsample.R
import com.github.flickrsample.utils.font.CustomTypeface


/*******************************************
 * View Extension Function
 *
 * Created by gk
 ******************************************/

/**
 * Make the view visible
 */
fun View.toVisible() {
    visibility = View.VISIBLE
}

/**
 * Make the view invisible
 */
fun View.toInvisible() {
    visibility = View.INVISIBLE
}

/**
 * Make the view gone
 */
fun View.toGone() {
    visibility = View.GONE
}

/**
 * Extension Function to change the font
 */
fun Toolbar.applyFontForToolbarTitle() {
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is TextView) {
            val titleFont = CustomTypeface[context.assets, "fonts/OpenSans-SemiBold.ttf"]
            if (view.text == title) {
                view.typeface = titleFont
                view.setTextColor(Color.WHITE)
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.toolbar_title))
                break
            }
        }
    }
}

/**
 * Loads the image into given image view
 *
 * FIXME: uses disk caching, set max size to avoid using lot of disk space,
 * NOTE : do cache cleanup for unused images
 *
 */
fun ImageView.loadImageFromLink(link: String?) {
    if (!link.isNullOrEmpty()) {
        Glide.with(context.applicationContext)
                .load(link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(this)
    }
}

/**
 * Loads the image into given image view, with progress bar as placeholder
 *
 * FIXME: uses disk caching, set max size to avoid using lot of disk space,
 * NOTE : do cache cleanup for unused images
 *
 */
fun ImageView.loadImageFromLinkWithLoadingProgress(link: String?) {
    if (!link.isNullOrEmpty()) {
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
                .into(this)
    }
}