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

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.flickrsample.R
import com.github.flickrsample.base.BaseMVPFragment
import com.github.flickrsample.data.models.local.PhotoItem
import com.github.flickrsample.ui.adapter.FullScreenImageAdapter
import com.github.flickrsample.utils.AppConstants
import com.github.flickrsample.utils.FlickrUtils

import kotlinx.android.synthetic.main.fragment_image_viewer.*

import javax.inject.Inject

/**
 * ImageViewer Fragment where images are displayed
 * Extends functionality of [BaseMVPFragment]
 * Implements Screen specific ui tasks [ImageViewerContract.View]
 *
 * Created by gk
 */
class ImageViewerFragment : BaseMVPFragment<ImageViewerContract.Presenter>(), ImageViewerContract.View {

    private lateinit var inflatedView: View

    private lateinit var photoId: String
    private var query: String? = null

    @Inject
    lateinit var mPresenter: ImageViewerContract.Presenter

    private var mCustomPagerAdapter: FullScreenImageAdapter? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getString(AppConstants.INTENT_ARGS_PHOTO_ID)?.let {
            photoId = it
        }
        arguments?.getString(AppConstants.INTENT_ARGS_QUERY)?.let {
            query = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflatedView = inflater.inflate(R.layout.fragment_image_viewer, container, false)
        return inflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val component = activityComponent
        component.inject(this)
        mPresenter.onAttach(this)

        mPresenter.init()
        mPresenter.loadPhotoFromPhotoId(FlickrUtils.API_KEY, query, photoId)

        back_button.setOnClickListener { activity?.finish() }
    }

    /**
     * Initialize the view pager to display photos
     */
    override fun initViewPager(photoItemList: List<PhotoItem>) {
        mCustomPagerAdapter = FullScreenImageAdapter(context!!, photoItemList)
        view_pager.adapter = mCustomPagerAdapter
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                mPresenter.loadNewPhotos(FlickrUtils.API_KEY, position)
            }

        })
    }

    /**
     * Updates the View pager if data set is changed
     */
    override fun refreshViewPager() {
        mCustomPagerAdapter?.notifyDataSetChanged()
    }

    /**
     * Set the current page of view pager
     */
    override fun updateCurrentPage(pageNo: Int) {
        if (pageNo > 0)
            view_pager.currentItem = pageNo
    }

    /**
     * Shows Empty UI
     */
    override fun showEmptyUI() {
        view_pager.visibility = View.GONE
        empty_photo_viewer.visibility = View.VISIBLE
    }

    /**
     * Shows progress bar
     */
    override fun showProgressBar() {
        next_image_loading.visibility = View.VISIBLE
    }

    /**
     * Hide progress bar
     */
    override fun hideProgressBar() {
        next_image_loading.visibility = View.GONE
    }

    override fun onDestroy() {
        mPresenter.onDetach()
        super.onDestroy()
    }

    companion object {
        fun newInstance(photoId: String, query: String) = ImageViewerFragment().apply {
            arguments = Bundle().apply {
                putString(AppConstants.INTENT_ARGS_PHOTO_ID, photoId)
                putString(AppConstants.INTENT_ARGS_QUERY, query)
            }
        }
    }
}
