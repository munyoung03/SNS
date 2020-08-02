package com.example.sns.view

import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityLoginBinding
import com.example.sns.retrofit.RetrofitClient
import com.example.sns.ui.dashboard.DashboardFragment
import com.example.sns.viewModel.LoginViewModel
import com.example.sns.widget.startActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel
        get() = getViewModel(LoginViewModel::class)

    override val layoutRes: Int
        get() = R.layout.activity_login

    override fun init() { viewModel.retrofit = RetrofitClient.getInstance()}


    override fun observerViewModel() {
        with(viewModel){
            loginBtn.observe(this@LoginActivity, Observer {
                getlogindata()
                if(checkLogin) {
                    Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(DashboardFragment::class.java)
                }
                else {
                    Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            })

            registerBtn.observe(this@LoginActivity, Observer {
                startActivity(RegisterActivity::class.java)
            })
        }
    }
}