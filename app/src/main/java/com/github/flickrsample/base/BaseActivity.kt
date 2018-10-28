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

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.github.flickrsample.di.component.ActivityComponent
import com.github.flickrsample.di.component.DaggerActivityComponent
import com.github.flickrsample.di.module.ActivityModule

/**
 * Base activity without any MVP component
 * Used to declare Activities that are dumb and only act as holder to other fragments
 *
 *
 * Created by gk
 */

abstract class BaseActivity : AppCompatActivity() {

    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((application as MainApplication).component)
                .build()

    }

    public override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }

}
