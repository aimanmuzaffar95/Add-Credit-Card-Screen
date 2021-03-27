package com.aiman.creditcardscreen.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aiman.creditcardscreen.utils.CardType

class MainViewModel : ViewModel() {

    val cardHolderName = MutableLiveData("")
    val cardNumber = MutableLiveData("")
    val cardNumberPlaceholder = MutableLiveData("**** **** **** ****")
    val cardExpiry = MutableLiveData("")
    val cardCVV = MutableLiveData("")
    val cardType = MutableLiveData(CardType.NO_TYPE)

}
