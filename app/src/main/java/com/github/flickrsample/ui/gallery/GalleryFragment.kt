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

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.flickrsample.R
import com.github.flickrsample.base.BaseMVPFragment
import com.github.flickrsample.data.models.local.PhotoItem
import com.github.flickrsample.ui.adapter.GalleryItemListAdapter
import com.github.flickrsample.ui.imageviewer.ImageViewerActivity
import com.github.flickrsample.utils.AppConstants
import com.github.flickrsample.utils.FlickrUtils

import kotlinx.android.synthetic.main.fragment_gallery.*

import javax.inject.Inject

/**
 * Gallery Fragment where images are displayed
 * Extends functionality of [BaseMVPFragment]
 * Implements Screen specific ui tasks [GalleryContract.View]
 *
 * Created by gk
 */
class GalleryFragment : BaseMVPFragment<GalleryContract.Presenter>(), GalleryContract.View {

    private lateinit var inflatedView: View

    @Inject
    lateinit var presenter: GalleryContract.Presenter

    private var itemAdapter: GalleryItemListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflatedView = inflater.inflate(R.layout.fragment_gallery, container, false)
        return inflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val component = activityComponent
        component.inject(this)
        presenter.onAttach(this)

        presenter.loadFirstPhotos(false, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)
    }

    /**
     * Sets the recycler view with Scroll Listener for pagination support
     */
    private fun setupRecyclerView() {

        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        item_recycler_view.layoutManager = gridLayoutManager

        //listen to scrolling, and calculate page number to load new items
        //custom support for pagination, improves performance
        val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = gridLayoutManager.childCount
                val totalItemCount = gridLayoutManager.itemCount
                val firstVisibleItems = IntArray(2)
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems)[0]
                if (!(presenter as GalleryPresenter).isLoading && !(presenter as GalleryPresenter).isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        presenter.loadNextPhotos(false, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)
                    }
                }
            }
        }

        //set the scroll listener
        item_recycler_view.addOnScrollListener(recyclerViewOnScrollListener)
    }


    /**
     * Initializes the list for the first time with photos data
     */
    override fun initItemList(photoItemList: List<PhotoItem>) {
        item_recycler_view.visibility = View.VISIBLE
        empty_list_text.visibility = View.GONE

        itemAdapter = GalleryItemListAdapter(context!!, photoItemList, object : GalleryItemListAdapter.ClickListener {
            override fun onClick(view: View?, position: Int) {
                if (view != null)
                    presenter.onImageClicked(position)
            }
        })
        item_recycler_view.adapter = itemAdapter
    }

    /**
     * Updates the list with photos data
     */
    override fun refreshItemList() {
        itemAdapter?.notifyDataSetChanged()
    }

    /**
     *  Shows empty photos list messages
     */
    override fun showEmptyListUI() {
        item_recycler_view.visibility = View.GONE
        empty_list_text.visibility = View.VISIBLE
    }

    /**
     *  Shows bottom loading when fetching new elements
     */
    override fun showBottomLoading() {
        bottom_progress_bar.visibility = View.VISIBLE
    }

    /**
     * Hide the bottom loading
     */
    override fun hideBottomLoading() {
        bottom_progress_bar.visibility = View.GONE
    }

    /**
     * Launch the ImageViewActivity with correct Initial Image
     */
    override fun launchImageViewActivity(photoId: String, query: String) {
        val intent = Intent(context, ImageViewerActivity::class.java)
        intent.putExtra(AppConstants.INTENT_ARGS_PHOTO_ID, photoId)
        intent.putExtra(AppConstants.INTENT_ARGS_QUERY, query)
        startActivity(intent)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    companion object {
        fun newInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }
}
