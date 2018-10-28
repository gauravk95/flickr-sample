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

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Utilities for Network related stuffs
 *
 * Created by gk
 */
object NetworkUtils {

    // Adds token as a header to the OkHttpClient making the request.
    val httpClient: OkHttpClient
        get() = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .method(original.method(), original.body())
                            .build()

                    chain.proceed(request)
                }
                .build()

    // Adds token as a header to the OkHttpClient making the request.
    fun getHttpClient(httpClient: OkHttpClient.Builder): OkHttpClient {
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .build()

            chain.proceed(request)
        }
        return httpClient.build()
    }

}
