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

import android.content.Context
import android.support.v7.app.AppCompatActivity

import com.github.flickrsample.data.source.repository.AppRepository
import com.github.flickrsample.di.ActivityContext
import com.github.flickrsample.ui.gallery.GalleryContract
import com.github.flickrsample.ui.gallery.GalleryPresenter
import com.github.flickrsample.ui.imageviewer.ImageViewerContract
import com.github.flickrsample.ui.imageviewer.ImageViewerPresenter
import com.github.flickrsample.utils.rx.AppSchedulerProvider
import com.github.flickrsample.utils.rx.SchedulerProvider

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Modules related to activity
 *
 * Created by gk.
 */

@Module
class ActivityModule(private val mActivity: AppCompatActivity) {

    @Provides
    @ActivityContext
    internal fun provideContext(): Context {
        return mActivity
    }

    @Provides
    internal fun provideActivity(): AppCompatActivity {
        return mActivity
    }

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    internal fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    internal fun provideGalleryPresenter(appRepository: AppRepository,
                                      schedulerProvider: SchedulerProvider,
                                      compositeDisposable: CompositeDisposable): GalleryContract.Presenter {
        return GalleryPresenter(appRepository, schedulerProvider, compositeDisposable)
    }

    @Provides
    internal fun provideImageViewerPresenter(appRepository: AppRepository,
                                      schedulerProvider: SchedulerProvider,
                                      compositeDisposable: CompositeDisposable): ImageViewerContract.Presenter {
        return ImageViewerPresenter(appRepository, schedulerProvider, compositeDisposable)
    }

}
