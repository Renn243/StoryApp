package com.raihanresa.storyapp.ui.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordCustom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    private lateinit var editText: TextInputEditText

    override fun onFinishInflate() {
        super.onFinishInflate()
        editText = (getEditText() ?: throw IllegalArgumentException("TextInputEditText required")) as TextInputEditText
        initTextWatcher()
    }

    private fun initTextWatcher() {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.length < 8) {
                        error = "The password must be at least 8 characters"
                    } else {
                        error = null
                    }
                }
            }
        })
    }
}