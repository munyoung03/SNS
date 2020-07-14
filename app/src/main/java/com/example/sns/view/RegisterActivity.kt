package com.example.sns.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sns.R
import com.example.sns.base.BaseActivity

class RegisterActivity : BaseActivity<V> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}