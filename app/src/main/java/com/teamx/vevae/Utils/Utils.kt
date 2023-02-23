package com.teamx.vevae.Utils

import android.view.View
import androidx.core.content.ContextCompat
import com.teamx.vevae.R
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(message : String){
/*    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_SHORT
    ).also {
        snackbar ->
        snackbar.setAction("Ok"){
            snackbar.dismiss()
        }
    }.show()*/

    val snackbar: Snackbar
    snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    val snackBarView = snackbar.getView()
    snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    snackbar.show()
}