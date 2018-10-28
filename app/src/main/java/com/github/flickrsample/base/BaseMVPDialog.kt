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

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.github.flickrsample.R
import com.github.flickrsample.utils.DialogUtils
import com.github.flickrsample.utils.GeneralUtils

/**
 * Acts a Base Dialog class for all other [DialogFragment] which will act as View part of MVP
 * Implements the basic functions as described in [BaseContract.View]
 *
 * Created by gk
 */

abstract class BaseMVPDialog<T> : DialogFragment(), BaseContract.View<T> {

    protected var progressDialog: Dialog? = null

    /**
     * Custom Progress Dialog
     */
    override fun showProgressDialog() {
        progressDialog = DialogUtils.createProgressDialog(context!!)
    }

    override fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun showToastMessage(message: String?) {
        if (GeneralUtils.checkStringNotEmpty(message))
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showToastMessage(@StringRes stringResourceId: Int) {
        showToastMessage(getString(stringResourceId))
    }

    override fun showSnackBarMessage(message: String?) {
        if (GeneralUtils.checkStringNotEmpty(message))
            showSnackBar(message)
    }

    override fun showSnackBarMessage(@StringRes stringResourceId: Int) {
        showSnackBarMessage(getString(stringResourceId))
    }

    /**
     * Creates a SnackBar for message display
     */
    private fun showSnackBar(message: String?) {
        if (activity != null) {
            val snackBar = Snackbar.make(activity!!.findViewById(android.R.id.content),
                    message as CharSequence, Snackbar.LENGTH_SHORT)
            val sbView = snackBar.view
            val textView = sbView
                    .findViewById<TextView>(android.support.design.R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            snackBar.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }

}
