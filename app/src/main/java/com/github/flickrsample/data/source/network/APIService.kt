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
package com.github.flickrsample.data.source.network

import com.github.flickrsample.data.models.remote.ResponsePhotoItemHolder
import com.github.flickrsample.utils.FlickrUtils

import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API services to connect to server
 *
 * Created by gk
 */

interface APIService {

    @GET("rest?format=json&nojsoncallback=1&method=" + FlickrUtils.METHOD_SEARCH + "&extras=url_" + FlickrUtils.SMALL_360 +
                "&safe_search=1")
    fun getImageItemList(@Query("api_key") key: String,
                         @Query("text") query: String,
                         @Query("page") page: Int,
                         @Query("per_page") perPage: Int): Flowable<Response<ResponsePhotoItemHolder>>

}
