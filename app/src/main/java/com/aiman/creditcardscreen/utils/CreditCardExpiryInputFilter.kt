package com.aiman.creditcardscreen.utils

import android.text.InputFilter
import android.text.Spanned
import java.text.SimpleDateFormat
import java.util.*

class CreditCardExpiryInputFilter : InputFilter {

    private var currentYearLastTwoDigits: String = SimpleDateFormat("yy", Locale.US).format(Date())

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {

        //do not insert if length is already 5
        if (dest.toString().length == 5) return ""

        //do not insert more than 1 character at a time
        if (source.length > 1) return ""

        //only allow character to be inserted at the end of the current text
        if (dest.isNotEmpty() && dstart != dest.length) return ""

        //if backspace, skip
        if (source.isEmpty()) {
            return source
        }

        //At this point, `source` is a single character being inserted at `dstart`.
        //`dstart` is at the end of the current text.

        val inputChar = source[0]

        if (dstart == 0) {
            //first month digit
            if (inputChar > '1') return ""
        }

        if (dstart == 1) {
            //second month digit
            val firstMonthChar = dest[0]
            if (firstMonthChar == '0' && inputChar == '0') return ""
            if (firstMonthChar == '1' && inputChar > '2') return ""
        }

        if (dstart == 2) {
            val currYearFirstChar = currentYearLastTwoDigits[0]
            return if (inputChar < currYearFirstChar) "" else "/$source"
        }

        if (dstart == 4) {
            val inputYear = "" + dest[dest.length - 1] + source.toString()
            if (inputYear.compareTo(currentYearLastTwoDigits) < 0) return ""
        }

        return source
    }

}