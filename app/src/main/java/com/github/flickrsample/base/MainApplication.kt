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
package com.github.flickrsample.base

import android.app.Application
import android.content.Context

import com.github.flickrsample.data.source.repository.AppDataSource
import com.github.flickrsample.data.source.repository.AppRepository
import com.github.flickrsample.di.component.ApplicationComponent
import com.github.flickrsample.di.component.DaggerApplicationComponent
import com.github.flickrsample.di.module.ApplicationModule
import com.github.flickrsample.di.module.DataModule
import com.github.flickrsample.di.module.NetworkModule
import com.github.flickrsample.utils.AppLogger

import javax.inject.Inject

/**
 * Entry place when application start
 * Good place to initialize stuff that has an Application Scope
 *
 * Created by gk
 */

class MainApplication : Application() {

    @Inject
    lateinit var mRepository: AppRepository

    // Needed to replace the component with a test specific one
    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .dataModule(DataModule())
                .networkModule(NetworkModule())
                .build()

        component.inject(this)

        instance = this

        (instance as MainApplication).initializeInstance()

    }

    // Here we do one-off initialisation which should apply to all activities
    // in the application.
    protected fun initializeInstance() {
        //globally initialize the App Logger
        AppLogger.init()
    }

    companion object {

        // Anywhere in the application where an instance is required, this method
        // can be used to retrieve it.
        lateinit var instance: Application
    }

}
