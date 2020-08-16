package com.example.sns.view.fragment

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMypageBinding
import com.example.sns.view.activity.LoginActivity
import com.example.sns.viewModel.MyPageViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageViewModel>() {

    override val viewModel: MyPageViewModel
        get() = getViewModel(MyPageViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_mypage

    override fun init() {
        binding.emailValue.text = MyApplication.prefs.getEmail("email", "no email")
        binding.username.text = MyApplication.prefs.getUsername("name", "no username")
    }

    override fun observerViewModel() {
        with(viewModel){
            logoutBtn.observe(this@MyPageFragment, Observer {
                setData()
                startActivity(LoginActivity::class.java)
            })
            image.observe(this@MyPageFragment, Observer {

            })
        }
    }

}