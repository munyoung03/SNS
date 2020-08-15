package com.example.sns.view.fragment

import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMainpageBinding
import com.example.sns.viewModel.MainPageViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainPageFragment : BaseFragment<FragmentMainpageBinding, MainPageViewModel>() {

    override val viewModel: MainPageViewModel
        get() = getViewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_mainpage

    override fun init() {

    }

    override fun observerViewModel() {

    }
}