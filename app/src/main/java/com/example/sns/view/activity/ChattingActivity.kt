package com.example.sns.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityChattingBinding
import com.example.sns.viewModel.ChattingViewModel
import com.example.sns.viewModel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ChattingActivity : BaseActivity<ActivityChattingBinding, ChattingViewModel>() {

    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.activity_chatting

    override fun init() {

    }

    override fun observerViewModel() {
    }

}