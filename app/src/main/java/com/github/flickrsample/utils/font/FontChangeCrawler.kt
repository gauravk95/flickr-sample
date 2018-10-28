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
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Used to change the font of a [ViewGroup] and its child
 *
 * Created by gk
 */

class FontChangeCrawler {
    private var typeface: Typeface? = null

    constructor(typeface: Typeface) {
        this.typeface = typeface
    }

    constructor(assets: AssetManager, assetsFontFileName: String) {
        typeface = TypefaceHelper.get(assets, assetsFontFileName)
    }

    fun replaceFonts(viewTree: ViewGroup) {
        var child: View
        for (i in 0 until viewTree.childCount) {
            child = viewTree.getChildAt(i)
            if (child is ViewGroup) {
                // recursive call
                replaceFonts(child)
            } else if (child is TextView) {
                // base case
                child.typeface = typeface
            }
        }
    }
}