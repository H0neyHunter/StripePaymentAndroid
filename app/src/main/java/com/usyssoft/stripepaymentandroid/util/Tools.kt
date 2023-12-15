package com.usyssoft.stripepaymentandroid.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

class Tools(private val context: Context) {
    fun hideKeyboard(edittext: EditText) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(edittext.windowToken, 0)
    }
}

fun EditText.timeHideKeyboard(){
    val handler = Handler(Looper.getMainLooper())
    this.setOnFocusChangeListener { view, b ->
        if (b) {
            handler.postDelayed({
                Tools(this.context).hideKeyboard(this)
            }, 3000)
        }
    }

    this.addTextChangedListener {
        handler.postDelayed({
            Tools(this.context).hideKeyboard(this)
        }, 3000)
    }
}