package com.bangkit.instadicoding.ui.customView

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.bangkit.instadicoding.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE

class EmailEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (it.contains("@") ) {
                    setTextColor(ContextCompat.getColor(context, R.color.dark_blue))
                    error = null
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                    setError("Invalid Email", null)
                }

            }
        }
    }
}