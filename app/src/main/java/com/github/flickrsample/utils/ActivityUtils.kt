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

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Utilities for Activities related stuffs
 *
 * Created by gk
 */

object ActivityUtils {

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     *
     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     *
     */
    fun addFragmentToActivity(fragmentManager: android.app.FragmentManager,
                              fragment: android.app.Fragment, frameId: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    /**
     * The `fragment` is added to the activity with tag `tag`. The operation is
     * performed by the `fragmentManager`.
     *
     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, tag: String) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(fragment, tag)
        transaction.commit()
    }
}
