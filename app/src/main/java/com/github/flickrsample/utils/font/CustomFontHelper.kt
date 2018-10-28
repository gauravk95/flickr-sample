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

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

import com.github.flickrsample.R

/**
 * Helper function to set Custom fonts
 *
 * Created by gk
 */

object CustomFontHelper {

    /**
     * Sets a font on a TextView based on the custom com.my.package:typeface attribute
     * If the custom font attribute isn't found in the attributes nothing happens
     * @param textView
     * @param context
     * @param attrs
     */
    fun setCustomFont(textView: TextView, context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomFont)
        val font = a.getString(R.styleable.CustomFont_typeface)
        setCustomFont(textView, font, context)
        a.recycle()
    }

    /**
     * Sets a font on a TextView
     * @param textView
     * @param font
     * @param context
     */
    fun setCustomFont(textView: TextView, tFont: String?, context: Context) {
        var font = tFont
        if (font == null) {
            font = "fonts/OpenSans-Light.ttf"
        }
        val tf = FontCache.get(font, context)
        if (tf != null) {
            textView.typeface = tf
        }
    }

}
