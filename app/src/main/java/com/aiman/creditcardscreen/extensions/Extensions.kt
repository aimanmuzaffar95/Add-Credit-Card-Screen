package com.aiman.creditcardscreen.extensions

import com.aiman.creditcardscreen.utils.CreditCardExpiryInputFilter
import com.aiman.creditcardscreen.utils.CreditCardFormatTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

object Extensions {
    fun TextInputEditText.setCreditCardTextWatcher() {
        val tv =
            CreditCardFormatTextWatcher(this)
        this.addTextChangedListener(tv)
    }

    fun TextInputEditText.setExpiryDateFilter() {
        this.filters = arrayOf(CreditCardExpiryInputFilter())
    }


}