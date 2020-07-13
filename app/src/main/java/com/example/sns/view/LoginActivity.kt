package com.example.sns.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityLoginBinding
import com.example.sns.retrofit.RetrofitClient
import com.example.sns.viewModel.LoginViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel
        get() = get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
        mBinding.executePendingBindings()

        mViewModel.retrofit = RetrofitClient.getInstance()

        with(mViewModel){
            btn.observe(this@LoginActivity, Observer {
                getlogindata()
                if(checkLogin == true) {
                    Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(MainPageFragment::class.java)
                }
                else if(checkLogin == false){
                    Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }

        with(mViewModel){
            btn.observe(this@LoginActivity, Observer {
                startActivity(RegisterActivity::class.java)
            })
        }

    }
}