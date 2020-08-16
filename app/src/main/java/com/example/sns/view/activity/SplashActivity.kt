package com.example.sns.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sns.R
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import kotlin.math.log

class SplashActivity : AppCompatActivity() {

    val checkLogin: Boolean = MyApplication.prefs.getCheckLogin("checklogin", false)
    var checkPer: Boolean = false

    var permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkPer = MyApplication.prefs.getCheckPermission("permission", false)

        if(checkPer) {
            login()
        }
        else{
            checkPermission()
        }
    }

    fun login() {
        if (checkLogin) {
            toast("자동로그인 성공")
            startActivity(MainActivity::class.java)
        } else {
            startActivity(LoginActivity::class.java)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun checkPermission() {
        Log.d("히힣", "1")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        Log.d("히힣", "2")
        for (permission in permission_list) {
            val chk = checkCallingOrSelfPermission(permission!!)
            if (chk == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permission_list, 0)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("히힣", "3")
        if (requestCode == 0) {
            Log.d("히힣", "4")
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    login()
                    MyApplication.prefs.setCheckPermission("permission", true)
                    Log.d("히힣", "5")
                } else {
                    Toast.makeText(applicationContext, "앱권한설정하세요", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}