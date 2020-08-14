package com.example.sns.view.activity

import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityLoginBinding
import com.example.sns.retrofit.RetrofitClient
import com.example.sns.viewModel.LoginViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import kotlinx.android.synthetic.main.activity_login.*
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
            })

            checkLogin.observe(this@LoginActivity, Observer {
                if(checkLogin.value == true) {
                    if(check_login.isChecked)
                    {
                        MyApplication.prefs.setCheckLogin("checklogin", true)
                    }
                    toast("로그인 성공")
                    startActivity(MainActivity::class.java)
                }
                else {
                    toast("로그인 실패")
                }
            })

            registerBtn.observe(this@LoginActivity, Observer {
                startActivity(RegisterActivity::class.java)
            })
        }
    }
}