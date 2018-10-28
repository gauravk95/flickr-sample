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
package com.github.flickrsample.di.module

import com.github.flickrsample.BuildConfig
import com.github.flickrsample.data.source.network.APIService
import com.github.flickrsample.data.source.network.NetworkAPIs
import com.github.flickrsample.data.source.network.NetworkUtils
import com.github.flickrsample.data.source.network.APIHelper
import com.github.flickrsample.utils.AppConstants

import java.util.concurrent.TimeUnit

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Modules related to network
 *
 * Created by gk.
 */

@Module
class NetworkModule {

    private var client: OkHttpClient.Builder? = null

    constructor()

    constructor(client: OkHttpClient.Builder) {
        this.client = client
    }

    @Provides
    @Singleton
    internal fun provideCall(): Retrofit {
        val retrofit: Retrofit
        if (client == null) {
            client = OkHttpClient.Builder()
                    .connectTimeout(AppConstants.NETWORK_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                    .writeTimeout(AppConstants.NETWORK_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                    .readTimeout(AppConstants.NETWORK_TIMEOUT_IN_SEC, TimeUnit.SECONDS)

            retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.FLICKR_BASE_URL)
                    .client(NetworkUtils.getHttpClient(client!!))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

        } else {
            retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.FLICKR_BASE_URL)
                    .client(NetworkUtils.httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

        return retrofit
    }

    @Provides
    @Singleton
    internal fun providesNetworkService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }

    @Provides
    @Singleton
    internal fun providesRetrofitHelper(apiHelper: APIHelper): NetworkAPIs {
        return apiHelper
    }

}
