package com.aiman.creditcardscreen.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.aiman.creditcardscreen.R
import com.aiman.creditcardscreen.databinding.ActivityMainBinding
import com.aiman.creditcardscreen.extensions.Extensions.setCreditCardTextWatcher
import com.aiman.creditcardscreen.extensions.Extensions.setExpiryDateFilter
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    lateinit var flipLeftIn: AnimatorSet
    lateinit var flipLeftOut: AnimatorSet
    lateinit var flipRightOut: AnimatorSet
    lateinit var flipRightIn: AnimatorSet

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

        viewModel.cardHolderName

        binding.btn.setOnClickListener {

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
            { value -> replacePlaceholder(value) }
        )
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
}