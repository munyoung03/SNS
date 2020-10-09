package com.example.sns.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sns.base.BaseViewModel
import com.example.sns.widget.SingleLiveEvent

class MainPageViewModel : BaseViewModel() {
    val pushBtn = SingleLiveEvent<Unit>()

    fun pushBtnClick()
    {
        pushBtn.call()
    }

}