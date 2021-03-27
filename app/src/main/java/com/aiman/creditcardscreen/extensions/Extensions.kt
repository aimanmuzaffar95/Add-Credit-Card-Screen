package com.aiman.creditcardscreen.extensions

import com.aiman.creditcardscreen.utils.CreditCardFormatTextWatcher
import com.google.android.material.textfield.TextInputEditText

object Extensions {
    fun TextInputEditText.setCreditCardTextWatcher() {
        val tv =
            CreditCardFormatTextWatcher(this)
        this.addTextChangedListener(tv)
    }

}