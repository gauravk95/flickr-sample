package com.github.flickrsample.utils.ext

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.Fragment
import com.github.flickrsample.R

/********************************************
 * Fragment Extension Functions
 *
 * Created by gk
 *********************************************/

/**
 * Creates a Progress Dialog
 * @return
 */
fun Fragment.createProgressDialog(): Dialog {
    val progressDialog = Dialog(context)
    progressDialog.show()
    if (progressDialog.window != null) {
        progressDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    progressDialog.setContentView(R.layout.progress_dialog)
    progressDialog.setCancelable(false)
    progressDialog.setCanceledOnTouchOutside(false)
    return progressDialog
}