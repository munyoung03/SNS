package com.example.sns.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sns.R
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import java.security.MessageDigest
import kotlin.math.log

class SplashActivity : AppCompatActivity() {

    val checkLogin: Boolean = MyApplication.prefs.getCheckLogin("checklogin", false)
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

        getHashKey()

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
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
        if (requestCode == 0) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    login()
                    MyApplication.prefs.setCheckPermission("permission", true)
                } else {
                    toast("퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요")
                    finish()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = info.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val key = String(Base64.encode(md.digest(), 0))
                Log.d("Hash key:", "!!!!!!!$key!!!!!!")
            }
        } catch(e: Exception) {
            Log.e("name not found", e.toString())
        }

    }
}