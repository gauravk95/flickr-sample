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
import android.preference.PreferenceManager

/**
 * Utilities for Shared Preferences
 *
 * Created by gk
 */

object PrefsUtils {

    //clears all the shared prefs
    fun clearSharedPrefs(sp: SharedPreferences) {
        sp.edit().clear().apply()
    }

    //GENERIC PREFS UTIL
    fun getLongSharedPref(context: Context, key: String): Long {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        return settings.getLong(key, 0)
    }

    fun getLongSharedPref(sp: SharedPreferences, key: String): Long {
        return sp.getLong(key, 0)
    }

    fun setLongSharedPref(context: Context, key: String, value: Long) {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = settings.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun setLongSharedPref(sp: SharedPreferences, key: String, value: Long) {
        val editor = sp.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getIntSharedPref(sp: SharedPreferences, key: String): Int {
        return sp.getInt(key, 0)
    }

    fun setIntSharedPref(sp: SharedPreferences, key: String, value: Int) {
        val editor = sp.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getBooleanSharedPref(sp: SharedPreferences, key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    fun setBooleanSharedPref(sp: SharedPreferences, key: String, value: Boolean) {
        val editor = sp.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getStringSharedPref(sp: SharedPreferences, key: String): String {
        return sp.getString(key, "")
    }

    fun setStringSharedPref(sp: SharedPreferences, key: String, value: String) {
        val editor = sp.edit()
        editor.putString(key, value)
        editor.apply()
    }
}
