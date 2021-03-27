package com.aiman.creditcardscreen.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.aiman.creditcardscreen.R
import com.aiman.creditcardscreen.utils.CardType
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText

object Bindings {
    @JvmStatic
    @BindingAdapter("cardType")
    fun changeBackGround(view: View, cardType: CardType) {
        when (cardType) {
            CardType.NO_TYPE -> {
                view.setBackgroundColor(view.context.getColor(R.color.blank_card))
            }
            CardType.VISA_CARD -> {
                view.setBackgroundColor(view.context.getColor(R.color.visa_card))
            }
            CardType.MASTER_CARD -> {
                view.setBackgroundColor(view.context.getColor(R.color.master_card))
            }
        }
    }

    @JvmStatic
    @BindingAdapter("cardType")
    fun setImage(imageView: ImageView, cardType: CardType) {
        when (cardType) {
            CardType.NO_TYPE -> {
                imageView.visibility = View.GONE
            }
            CardType.VISA_CARD -> {
                imageView.visibility = View.VISIBLE
                Glide.with(imageView.context).load(R.drawable.ic_visa_logo).into(imageView)
            }
            CardType.MASTER_CARD -> {
                imageView.visibility = View.VISIBLE
                Glide.with(imageView.context).load(R.drawable.ic_master_card).into(imageView)
            }
        }
    }
}