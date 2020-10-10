package com.example.sns.view.activity

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityLoginBinding
import com.example.sns.retrofit.RetrofitClient
import com.example.sns.viewModel.LoginViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import com.facebook.*
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    lateinit var callBackManager : CallbackManager

    override val viewModel: LoginViewModel
        get() = getViewModel(LoginViewModel::class)

    override val layoutRes: Int
        get() = R.layout.activity_login

    override fun init() { viewModel.retrofit = RetrofitClient.getInstance()}


    override fun observerViewModel() {
        with(viewModel){
            loginBtn.observe(this@LoginActivity, Observer {
                login()
            })

            status.observe(this@LoginActivity, Observer {
                if(status.value == "200") {
                    if(check_login.isChecked)
                    {
                        MyApplication.prefs.setCheckLogin("checklogin", true)
                    }
                    toast("로그인 성공")
                    startActivity(MainActivity::class.java)
                }
                else {
                    Log.d("TAG", "사유 : ${status.value}")
                    toast("로그인 실패")
                }
            })

            registerBtn.observe(this@LoginActivity, Observer {
                startActivity(RegisterActivity::class.java)
            })

            faceBookLoginBtn.observe(this@LoginActivity, Observer {
                btn_facebook_login.setPermissions(listOf("email", "public_profile"))
                callBackManager = CallbackManager.Factory.create()
                btn_facebook_login.registerCallback(callBackManager, object : FacebookCallback<LoginResult>{
                    override fun onSuccess(result: LoginResult?) {
                        if(result?.accessToken != null)
                        {
                            val accessToken = result.accessToken
                            getFacebookInfo(accessToken)
                            startActivity(MainActivity::class.java)
                        }else{
                            Log.d("TAG", "access token is null")
                        }
                    }

                    override fun onCancel() {
                        TODO("Not yet implemented")
                    }

                    override fun onError(error: FacebookException?) {
                        TODO("Not yet implemented")
                    }

                })
            })

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getFacebookInfo(accessToken: AccessToken){

        val graphRequest : GraphRequest = GraphRequest.newMeRequest(accessToken
        ) { resultObject, response ->
            try {
                val name = resultObject.getString("name")
                val email = resultObject.getString("email")
                val image = resultObject.getJSONObject("picture").getJSONObject("data").getString("url")
                Log.d("TAG", "이름 $name + 이메일 $email + 이미지 $image")
                toast(name.toString())
            }catch (e : JSONException){
                e.printStackTrace()
            }
        }

        var parameters : Bundle = Bundle()
        parameters.putString("fields", "id,name,picture.width(200)")
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }
}