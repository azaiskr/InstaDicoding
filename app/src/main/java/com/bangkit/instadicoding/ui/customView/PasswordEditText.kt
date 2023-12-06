package com.bangkit.instadicoding.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
            validatePasswordStrength(text)
        }
        val passwordToggleDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_key_24)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, passwordToggleDrawable, null)
        setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun togglePasswordVisibility() {
        inputType = if (inputType == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        setSelection(text?.length ?: 0)
    }

    private fun validatePasswordStrength(password: CharSequence?) {
        val isStrongPassword = (password?.length ?: 0) >= 8
        setError(isStrongPassword)
    }

    private fun setError(isValid: Boolean) {
        error = if (isValid) {
            // Password is strong
            setTextColor(ContextCompat.getColor(context, R.color.dark_blue))
            null
        } else {
            // Password is weak
            setTextColor(ContextCompat.getColor(context, R.color.red))
            context.getString(R.string.password_validation)
        }
    }
}