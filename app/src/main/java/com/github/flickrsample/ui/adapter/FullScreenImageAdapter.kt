package com.github.flickrsample.ui.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.github.flickrsample.R
import com.github.flickrsample.data.models.local.PhotoItem
import com.github.flickrsample.utils.FlickrUtils
import com.github.flickrsample.utils.ext.loadImageFromLinkWithLoadingProgress

/**
 * Custom View Pager to support image sliding
 */
//FIXME: Use a Fixed MAX_SIZE for the ViewPager to avoid OUT_OF_MEMORY error, keep most recent and active items in it
class FullScreenImageAdapter constructor(
        private val mContext: Context,
        private val mPhotoItems: List<PhotoItem>) : PagerAdapter() {

    override fun getItemPosition(obj: Any): Int {
        val dummyItem = (obj as RelativeLayout).tag as PhotoItem
        val position = mPhotoItems.indexOf(dummyItem)
        return if (position >= 0) {
            // The current data matches the data in this active fragment, so let it be as it is.
            position
        } else {
            // Returning POSITION_NONE means the current data does not matches the data this fragment is showing right now.
            // Returning POSITION_NONE constant will force the fragment to redraw its view layout all over again and show new data.
            PagerAdapter.POSITION_NONE
        }
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = inflater.inflate(R.layout.pager_image_viewer, container,
                false)

        val imgDisplay = viewLayout.findViewById<View>(R.id.photo_view) as ImageView
        val textDisplay = viewLayout.findViewById<View>(R.id.photo_view_text) as TextView
        val photoItem = mPhotoItems[position]

        if (!photoItem.title.isNullOrEmpty())
            textDisplay.text = photoItem.title
        else
            textDisplay.visibility = View.GONE

        imgDisplay.loadImageFromLinkWithLoadingProgress(
                FlickrUtils.getFlickrImageLink(photoItem.id,
                        photoItem.secret, photoItem.server, photoItem.farm,
                        FlickrUtils.LARGE_1600)) //LARGE_2048 image unavailable

        (container as ViewPager).addView(viewLayout)

        viewLayout.tag = photoItem

        return viewLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        (container as ViewPager).removeView(obj as RelativeLayout)
    }

    override fun getCount(): Int = mPhotoItems.size

}