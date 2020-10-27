package com.example.sns.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sns.R
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SplashActivity : AppCompatActivity() {

    val checkLogin: String = MyApplication.prefs.getCheckLogin( "null")
    var checkPer: Boolean = false

    var permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        checkPer = MyApplication.prefs.getCheckPermission(false)

        if (checkPer) {
            login()
        } else {
            checkPermission()
        }
    }

    fun login() {
        if (checkLogin == "normal login") {
            toast("자동로그인 성공")
            startActivity(MainActivity::class.java)
        } else if (checkLogin == "facebook login") {
            toast("자동로그인 성공")
            val accessToken = AccessToken.getCurrentAccessToken()
            if (accessToken !== null) {
                startActivity(MainActivity::class.java)
            } else {
                toast("세션 만료")
                startActivity(LoginActivity::class.java)
            }
        } else if (checkLogin == "google login") {
            toast("자동로그인 성공")
            val accessToken = GoogleSignIn.getLastSignedInAccount(this)
            if (accessToken !== null) {
                startActivity(MainActivity::class.java)
            } else {
                toast("세션 만료")
                startActivity(LoginActivity::class.java)
            }
        } else {
            LoginManager.getInstance().logOut()
            startActivity(LoginActivity::class.java)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun checkPermission() {
        for (permission in permission_list) {
            val chk = checkCallingOrSelfPermission(permission)
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
        if (requestCode == 0) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    login()
                    MyApplication.prefs.setCheckPermission(true)
                } else {
                    toast("퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요")
                    finish()
                }
            }
        }
    }
}