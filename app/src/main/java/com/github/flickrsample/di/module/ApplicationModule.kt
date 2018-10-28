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

import android.app.Application
import android.content.Context

import com.github.flickrsample.data.source.repository.AppDataRepository
import com.github.flickrsample.data.source.repository.AppRepository
import com.github.flickrsample.di.ApplicationContext
import com.github.flickrsample.di.Remote

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Modules related to application
 *
 * Created by gk.
 */

@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    @Provides
    internal fun provideApplication(): Application {
        return mApplication
    }

}
