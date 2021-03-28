package com.aiman.creditcardscreen.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.aiman.creditcardscreen.R
import com.aiman.creditcardscreen.databinding.ActivityMainBinding
import com.aiman.creditcardscreen.extensions.Extensions.setCreditCardTextWatcher
import com.aiman.creditcardscreen.extensions.Extensions.setExpiryDateFilter
import com.aiman.creditcardscreen.utils.CardType
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import render.animations.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    lateinit var flipLeftIn: AnimatorSet
    lateinit var flipLeftOut: AnimatorSet
    lateinit var flipRightOut: AnimatorSet
    lateinit var flipRightIn: AnimatorSet

    lateinit var animatedVectorDrawable: AnimatedVectorDrawable

    private var isFront = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
            frontSide.viewModel = this@MainActivity.viewModel
            frontSide.lifecycleOwner = this@MainActivity
            backSide.viewModel = this@MainActivity.viewModel
            backSide.lifecycleOwner = this@MainActivity
        }

        setupAnimation()
        setupViews()

    }

    private fun flipCard() {
        if (isFront) {
            flipRightIn.setTarget(binding.backSide.backSide)
            flipRightOut.setTarget(binding.frontSide.frontSide)
            flipRightIn.start()
            flipRightOut.start()
        } else {
            flipLeftIn.setTarget(binding.frontSide.frontSide)
            flipLeftOut.setTarget(binding.backSide.backSide)
            flipLeftIn.start()
            flipLeftOut.start()
        }
        isFront = !isFront
    }

    private fun setupAnimation() {
        val scale = applicationContext.resources.displayMetrics.density
        binding.frontSide.frontSide.cameraDistance = 16000 * scale
        binding.backSide.backSide.cameraDistance = 16000 * scale

        flipLeftIn =
            AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_in) as AnimatorSet
        flipLeftOut =
            AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_out) as AnimatorSet
        flipRightIn =
            AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_in) as AnimatorSet
        flipRightOut =
            AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_out) as AnimatorSet
    }

    private fun setupViews() {
        binding.etCardNumber.setCreditCardTextWatcher()
        binding.etExpiryDate.setExpiryDateFilter()

        viewModel.cardNumber.observe(this,
            { value ->
                replacePlaceholder(value)
                checkCardType(value)
            }
        )

        binding.etCardCvv.setOnFocusChangeListener { view, b ->
            if (b) {
                flipCard()
            } else {
                MainScope().launch {
                    delay(400)
                    flipCard()
                }
            }
        }
    }

    private fun replacePlaceholder(value: String) {
        var placeholderString = "**** **** **** ****"

        value.forEach {
            val sb = StringBuilder(placeholderString)
            val firstIndex = placeholderString.indexOf("*")
            sb.setCharAt(firstIndex, it)
            placeholderString = sb.toString()
        }
        viewModel.cardNumberPlaceholder.postValue(placeholderString)
    }

    private fun checkCardType(value: String) {
        if (value.length < 4) {
            viewModel.cardType.value = CardType.NO_TYPE
            return
        }

        if (value.startsWith("4")) {
            viewModel.cardType.value = CardType.VISA_CARD
        } else {
            viewModel.cardType.value = CardType.MASTER_CARD
        }
    }

    private fun saveCard() {
        viewModel.cardSaved.value = true
        var render = Render(this).also { it.setAnimation(Slide.InUp(binding.tempImg)) }
        render.start()

        MainScope().launch {
            delay(1000)
            binding.tvCardSaved.visibility = View.VISIBLE
            var textRender =
                Render(this@MainActivity).also { it.setAnimation(Fade.InUp(binding.tvCardSaved)) }
            textRender.start()
            delay(3000)
//            render = Render(this@MainActivity).also { it.setAnimation(Fade.Out(binding.tempImg)) }
            render = Render(this@MainActivity).also { it.setAnimation(Flip.OutX(binding.tempImg)) }
            textRender =
                Render(this@MainActivity).also { it.setAnimation(Fade.Out(binding.tvCardSaved)) }
            render.start()
            textRender.start()
        }

        MainScope().launch {
            delay(1000)
            val drawable = binding.done.drawable
            animatedVectorDrawable = drawable as AnimatedVectorDrawable
            animatedVectorDrawable.start()
        }

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.save_btn -> saveCard()
            R.id.btn -> flipCard()
        }
    }
}