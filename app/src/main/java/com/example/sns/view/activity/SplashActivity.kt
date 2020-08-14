package com.example.sns.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sns.R
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val checkLogin: Boolean = MyApplication.prefs.getCheckLogin("checklogin", false)

        if(checkLogin)
        {
            toast("자동로그인 성공")
            startActivity(MainActivity::class.java)
        }
        else
        {
            startActivity(LoginActivity::class.java)
        }
    }
}