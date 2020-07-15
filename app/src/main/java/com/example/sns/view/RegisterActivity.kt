package com.example.sns.view

import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityRegisterBinding
import com.example.sns.retrofit.RetrofitClient
import com.example.sns.ui.dashboard.DashboardFragment
import com.example.sns.viewModel.RegisterViewModel
import com.example.sns.widget.startActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>() {

    override val viewModel : RegisterViewModel
        get() = getViewModel (RegisterViewModel::class)

    override val layoutRes: Int
        get() = (R.layout.activity_register)

    override fun init() {  viewModel.retrofit = RetrofitClient.getInstance() }

    override fun observerViewModel() {
        with(viewModel){
            btn.observe(this@RegisterActivity, Observer {
                sendRegisterData()
                if(checkLogin == true) {
                    Toast.makeText(applicationContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    startActivity(DashboardFragment::class.java)
                }
                else if(checkLogin == false){
                    Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}