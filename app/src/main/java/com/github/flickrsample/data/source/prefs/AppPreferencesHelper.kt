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

import android.content.Context
import android.content.SharedPreferences

import com.github.flickrsample.di.ApplicationContext
import com.github.flickrsample.di.PreferenceInfo

import javax.inject.Inject
import javax.inject.Singleton

/**
 * An Helper class for Shared Preferences
 *
 * Created by gk.
 */

@Singleton
class AppPreferencesHelper @Inject
constructor(@ApplicationContext context: Context,
            @PreferenceInfo prefFileName: String) : PreferencesHelper {

    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override fun getBoolean(key: String): Boolean {
        return PrefsUtils.getBooleanSharedPref(mPrefs, key)
    }

    override fun getLong(key: String): Long {
        return PrefsUtils.getLongSharedPref(mPrefs, key)
    }

    override fun getInt(key: String): Int {
        return PrefsUtils.getIntSharedPref(mPrefs, key)
    }

    override fun getString(key: String): String {
        return PrefsUtils.getStringSharedPref(mPrefs, key)
    }

    override fun setBoolean(key: String, value: Boolean) {
        PrefsUtils.setBooleanSharedPref(mPrefs, key, value)
    }

    override fun setLong(key: String, value: Long) {
        PrefsUtils.setLongSharedPref(mPrefs, key, value)
    }

    override fun setInt(key: String, value: Int) {
        PrefsUtils.setIntSharedPref(mPrefs, key, value)
    }

    override fun getString(key: String, value: String) {
        PrefsUtils.setStringSharedPref(mPrefs, key, value)
    }

}
