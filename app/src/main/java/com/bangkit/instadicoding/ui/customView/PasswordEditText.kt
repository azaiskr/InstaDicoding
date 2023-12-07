package com.bangkit.instadicoding.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.bangkit.instadicoding.R
import com.google.android.material.textfield.TextInputEditText

@SuppressLint("ViewConstructor")
class PasswordEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        doOnTextChanged { text, _, _, _ ->
            text?.let{
                if(it.length <8 ) {
                    setError("Password must be at least 8 characters",null)
                    setTextColor(ContextCompat.getColor(context,R.color.red))
                } else {
                    setTextColor(ContextCompat.getColor(context,R.color.dark_blue))
                }
            }
        }
    }
}