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
package com.github.flickrsample.utils.font

import android.content.res.AssetManager
import android.graphics.Typeface

import com.github.flickrsample.utils.AppLogger

import java.util.Hashtable

/**
 * Another helper function to cache and provide typeface
 *
 * Created by gk
 */

object TypefaceHelper {
    private val TAG = "TypefaceHelper"

    private val cache = Hashtable<String, Typeface>()

    operator fun get(am: AssetManager, assetPath: String): Typeface? {
        if (!cache.containsKey(assetPath)) {
            try {
                val t = Typeface.createFromAsset(am,
                        assetPath)
                cache[assetPath] = t
            } catch (e: Exception) {
                AppLogger.e(TAG, "Could not get typeface '" + assetPath
                        + "' because " + e.message)
                return null
            }

        }
        return cache[assetPath]
    }
}