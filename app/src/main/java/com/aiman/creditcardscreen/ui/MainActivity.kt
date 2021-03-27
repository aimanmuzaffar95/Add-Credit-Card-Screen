package com.aiman.creditcardscreen.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.aiman.creditcardscreen.utils.CreditCardFormatTextWatcher
import com.aiman.creditcardscreen.R
import com.aiman.creditcardscreen.databinding.ActivityMainBinding
import com.aiman.creditcardscreen.extensions.Extensions.setCreditCardTextWatcher


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var flipLeftIn: AnimatorSet
    lateinit var flipLeftOut: AnimatorSet
    lateinit var flipRightOut: AnimatorSet
    lateinit var flipRightIn: AnimatorSet

    private var isFront = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        setupAnimation()
        setupViews()

        binding.btn.setOnClickListener {

            if (isFront) {
                flipRightIn.setTarget(binding.backSide)
                flipRightOut.setTarget(binding.frontSide)
                flipRightIn.start()
                flipRightOut.start()
            } else {
                flipLeftIn.setTarget(binding.frontSide)
                flipLeftOut.setTarget(binding.backSide)
                flipLeftIn.start()
                flipLeftOut.start()
            }
            isFront = !isFront

        }
    }

    private fun setupAnimation() {
        val scale = applicationContext.resources.displayMetrics.density
        binding.frontSide.cameraDistance = 16000 * scale
        binding.backSide.cameraDistance = 16000 * scale

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
    }
}