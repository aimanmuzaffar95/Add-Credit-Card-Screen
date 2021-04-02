package com.aiman.creditcardscreen.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ReplacementSpan
import android.util.TypedValue
import android.widget.TextView

class CreditCardFormatTextWatcher : TextWatcher {

    private val NO_MAX_LENGTH = -1
    private var maxLength = NO_MAX_LENGTH
    private var paddingPx = 0
    private var internalStopFormatFlag = false

    /**
     * Create a credit card formatter with no max length and a padding specified in pixels
     *
     * @param paddingPx padding in pixels unit
     */

    constructor(paddingPx: Int) {
        setPaddingPx(paddingPx)
    }

    /**
     * Create a credit card formatter with no max length and a padding of 1 em (depends on text size).
     * <p>
     * The padding is not automatically updated if the text size or typeface are changed in the textview).
     *
     * @param textView the widget you want to format
     */

    constructor(textView: TextView) {
        setPaddingEm(textView, 1f)
    }

    /**
     * Create a credit card formatter with no max length and a padding specified in em (depends on text size).
     * <p>
     * The padding is not automatically updated if the text size or typeface are changed in the textview).
     *
     * @param textView  the widget you want to format
     * @param paddingEm padding in em unit (character size unit)
     */

    constructor(textView: TextView, paddingEm: Float) {
        setPaddingEm(textView, paddingEm)
    }

    /**
     * Create a credit card formatter with no max length and a padding specified in SP Unit (depends on the scale applied to text).
     *
     * @param context   any Context
     * @param paddingSp the padding in SP unit
     */
    constructor(context: Context, paddingSp: Float) {
        setPaddingSp(context, paddingSp)
    }

    /**
     * Change the padding, do not take effect until next text change
     *
     * @param paddingPx padding in pixels unit
     */
    fun setPaddingPx(paddingPx: Int) {
        this.paddingPx = paddingPx
    }

    /**
     * Change the padding, do not take effect until next text change
     *
     * @param textView the widget you want to format
     * @param em       padding in em unit (character size unit)
     */
    fun setPaddingEm(textView: TextView, em: Float) {
        val emSize = textView.paint.measureText("x")
        setPaddingPx((em * emSize).toInt())
    }

    /**
     * Change the padding, do not take effect until next text change
     *
     * @param context   any Context
     * @param paddingSp the padding in SP unit
     */
    fun setPaddingSp(context: Context, paddingSp: Float) {
        setPaddingPx(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                paddingSp,
                context.resources.displayMetrics
            )
                .toInt()
        )
    }

    /**
     * Change maxLength of the credit card number, does not take effect until next text change
     *
     * @param maxLength new max length
     */
    fun setMaxLength(maxLength: Int) {
        this.maxLength = maxLength
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (internalStopFormatFlag) {
            return
        }
        internalStopFormatFlag = true
        CreditCardFormatTextWatcher.formatCardNumber(s!!, paddingPx, maxLength)
        internalStopFormatFlag = false
    }

    /**
     * Format the provided widget card number (useful if you want to reformat it after changing padding)
     *
     * @param textView the widget containing the credit card number
     */
    fun formatCardNumber(textView: TextView) {
        afterTextChanged(textView.editableText)
    }


    companion object {

        private const val NO_MAX_LENGTH = -1

        fun formatCardNumber(ccNumber: Editable, paddingPx: Int) {
            CreditCardFormatTextWatcher.formatCardNumber(
                ccNumber,
                paddingPx,
                CreditCardFormatTextWatcher.NO_MAX_LENGTH
            )
        }

        fun formatCardNumber(ccNumber: Editable, paddingPx: Int, maxLength: Int) {
            val textLength = ccNumber.length
            // first remove any previous span
            val spans: Array<CreditCardFormatTextWatcher.PaddingRightSpan> = ccNumber.getSpans(
                0, ccNumber.length,
                CreditCardFormatTextWatcher.PaddingRightSpan::class.java
            )
            for (i in spans.indices) {
                ccNumber.removeSpan(spans[i])
            }
            // then truncate to max length
            if (maxLength > 0 && textLength > maxLength - 1) {
                ccNumber.replace(maxLength, textLength, "")
            }
            // finally add margin spans
            for (i in 1..(textLength - 1) / 4) {
                val end = i * 4
                val start = end - 1
                val marginSPan = CreditCardFormatTextWatcher.PaddingRightSpan(paddingPx)
                ccNumber.setSpan(marginSPan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    class PaddingRightSpan(private val mPadding: Int) : ReplacementSpan() {
        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fm: FontMetricsInt?
        ): Int {
            val widths = FloatArray(end - start)
            paint.getTextWidths(text, start, end, widths)
            var sum = mPadding
            for (i in widths.indices) {
                sum += widths[i].toInt()
            }
            return sum
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        }
    }
}