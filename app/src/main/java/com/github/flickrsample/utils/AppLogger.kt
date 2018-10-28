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
package com.github.flickrsample.utils

import android.util.Log

import com.github.flickrsample.BuildConfig

/**
 * An App logger to only log in Debug mode
 *
 * Created by gk
 */

object AppLogger {

    private var isDebugMode = true

    fun init() {
        if (!BuildConfig.DEBUG) {
            isDebugMode = false
        }
    }

    fun d(tag: String, msg: String) {
        if (isDebugMode)
            Log.d(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (isDebugMode)
            Log.e(tag, msg)
    }

    fun w(tag: String, msg: String) {
        if (isDebugMode)
            Log.w(tag, msg)
    }

    fun i(tag: String, msg: String) {
        if (isDebugMode)
            Log.i(tag, msg)
    }

    fun v(tag: String, msg: String) {
        if (isDebugMode)
            Log.v(tag, msg)
    }

}
