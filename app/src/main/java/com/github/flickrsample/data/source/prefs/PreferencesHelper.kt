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
package com.github.flickrsample.data.source.prefs

/**
 * Created by gk.
 */

interface PreferencesHelper {

    //GENERIC
    fun getBoolean(key: String): Boolean

    fun getLong(key: String): Long

    fun getInt(key: String): Int

    fun getString(key: String): String

    fun setBoolean(key: String, value: Boolean)

    fun setLong(key: String, value: Long)

    fun setInt(key: String, value: Int)

    fun getString(key: String, value: String)

}
