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
package com.github.flickrsample.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.github.flickrsample.R
import com.github.flickrsample.data.models.local.PhotoItem
import com.github.flickrsample.ui.custom.RoundedCornerImageView
import com.github.flickrsample.utils.FlickrUtils
import com.github.flickrsample.utils.GeneralUtils

/**
 * Adapter that used to display [PhotoItem] in a recycler view
 *
 * Created by gk
 */
class GalleryItemListAdapter(
        private val mContext: Context,
        private val mPhotoItems: List<PhotoItem>,
        private val mListener: ClickListener?) :
        RecyclerView.Adapter<GalleryItemListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_gallery, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mPhotoItems[position]
        var thumbnailLink = item.url_n
        if(thumbnailLink.isNullOrEmpty())
            thumbnailLink = FlickrUtils.getFlickrImageLink(item.id, item.secret, item.server, item.farm, FlickrUtils.SMALL_360)

        GeneralUtils.loadImageFromLink(mContext, holder.image, thumbnailLink!!)

        if (!item.width_n.isNullOrEmpty())
            holder.image.setHeightRatio(calculateHeightRatio(item.width_n!!, item.height_n!!))

        if(!item.title.isNullOrEmpty())
            holder.title.text = item.title
        else
            holder.title.visibility = View.GONE

    }

    private fun calculateHeightRatio(width_n: String, height_n: String): Float {
        val w = width_n.toInt()
        val h = height_n.toInt()

        return (h.toFloat()/w.toFloat())
    }

    override fun getItemCount(): Int {
        return mPhotoItems.size
    }

    /**
     * View Holder for recycler view.
     */
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: RoundedCornerImageView = itemView.findViewById(R.id.item_image)
        var title: TextView = itemView.findViewById(R.id.item_text)

        init {
            image.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mListener?.onClick(p0, adapterPosition)
        }
    }

    interface ClickListener{
        fun onClick(view: View?, position: Int)
    }
}