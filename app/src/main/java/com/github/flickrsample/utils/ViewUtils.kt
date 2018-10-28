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

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.View
import android.widget.TextView

import com.github.flickrsample.R
import com.github.flickrsample.utils.font.TypefaceHelper

/**
 * Utilities for View related stuffs
 *
 * Created by gk
 */

object ViewUtils {

    fun pxToDp(px: Float): Float {
        val densityDpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
        return px / (densityDpi / 160f)
    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return Math.round(dp * density)
    }

    fun applyFontForToolbarTitle(context: Activity, resId: Int) {
        val toolbar = context.findViewById<View>(resId) as Toolbar
        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view is TextView) {
                val titleFont = TypefaceHelper.get(context.assets, "fonts/OpenSans-SemiBold.ttf")
                if (view.text == toolbar.title) {
                    view.typeface = titleFont
                    view.setTextColor(Color.WHITE)
                    view.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.toolbar_title))
                    break
                }
            }
        }
    }

}
