package com.example.sns.viewModel

import com.example.sns.base.BaseViewModel
import com.example.sns.widget.SingleLiveEvent

class MainPageViewModel : BaseViewModel() {
    val btn = SingleLiveEvent<Unit>()

    fun btnClick() {
        btn.call()
    }


}