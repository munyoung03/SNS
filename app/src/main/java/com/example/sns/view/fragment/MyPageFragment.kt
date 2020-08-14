package com.example.sns.view.fragment

import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMypageBinding
import com.example.sns.viewModel.MyPageViewModel
import com.example.sns.widget.MyApplication
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageViewModel>() {

    override val viewModel: MyPageViewModel
        get() = getViewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_mypage

    override fun init() {
        binding.emailValue.text = MyApplication.prefs.getEmail("email", "no email")
        binding.username.text = MyApplication.prefs.getUsername("name", "no username")
    }

    override fun observerViewModel() {

    }
}