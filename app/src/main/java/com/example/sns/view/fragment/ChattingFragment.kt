package com.example.sns.view.fragment

import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentChattingBinding
import com.example.sns.viewModel.ChattingViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ChattingFragment : BaseFragment<FragmentChattingBinding, ChattingViewModel>() {

    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_chatting

    override fun init() {


    }

    override fun observerViewModel() {

    }

}