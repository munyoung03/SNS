package com.example.sns.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityLoginBinding
import com.example.sns.network.retrofit.RetrofitClient
import com.example.sns.viewModel.LoginViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    lateinit var callBackManager: CallbackManager

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 99

    override val viewModel: LoginViewModel
        get() = getViewModel(LoginViewModel::class)

    override val layoutRes: Int
        get() = R.layout.activity_login

    override fun init() {

        viewModel.retrofit = RetrofitClient.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            //'R.string.default_web_client_id' 에는 본인의 클라이언트 아이디를 넣어주시면 됩니다.
            //저는 스트링을 따로 빼서 저렇게 사용했지만 스트링을 통째로 넣으셔도 됩니다.
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //firebase auth 객체
        firebaseAuth = FirebaseAuth.getInstance()
    }


    override fun observerViewModel() {
        with(viewModel) {
            loginBtn.observe(this@LoginActivity, Observer {
                login()
            })

            status.observe(this@LoginActivity, Observer {
                if (it == "200") {
                    if (check_login.isChecked) {
                        MyApplication.prefs.setCheckLogin("checklogin", "normal login")
                    }
                    toast("로그인 성공")
                    startActivity(MainActivity::class.java)
                } else {
                    Log.d("TAG", "사유 : ${status.value}")
                    toast("로그인 실패")
                }
            })

            googleLoginBtn.observe(this@LoginActivity, Observer {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            })

            registerBtn.observe(this@LoginActivity, Observer {
                startActivity(RegisterActivity::class.java)
            })

            faceBookLoginBtn.observe(this@LoginActivity, Observer {
                //페이스북 연동 성공시 받아올 데이터 정의
                btn_facebook_login.setPermissions(listOf("email", "public_profile"))
                callBackManager = CallbackManager.Factory.create()
                //통신
                btn_facebook_login.registerCallback(
                    callBackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult?) {
                            if (result?.accessToken != null) {
                                val accessToken = result.accessToken
                                getFacebookInfo(accessToken)
                                startActivity(MainActivity::class.java)
                                if (check_login.isChecked) {
                                    MyApplication.prefs.setCheckLogin(
                                        "checklogin",
                                        "facebook login"
                                    )
                                }
                            } else {
                                Log.d("TAG", "access token is null")
                            }
                        }

                        override fun onCancel() {
                        }

                        override fun onError(error: FacebookException?) {
                        }

                    })
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        } else {
            callBackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getFacebookInfo(accessToken: AccessToken) {

        val graphRequest: GraphRequest = GraphRequest.newMeRequest(
            accessToken
        ) { resultObject, response ->
            try {
                val name = resultObject.getString("name")
                val email = resultObject.getString("email")
                val image =
                    resultObject.getJSONObject("picture").getJSONObject("data").getString("url")
                Log.d("TAG", "이름 $name + 이메일 $email + 이미지 $image")
                MyApplication.prefs.setUsername("name", name)
                MyApplication.prefs.setEmail("email", email)
                toast(name.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        var parameters: Bundle = Bundle()
        parameters.putString("fields", "id,name,picture.width(200)")
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + acct.id!!)

        //Google SignInAccount 객체에서 ID 토큰을 가져와서 Firebase Auth로 교환하고 Firebase에 인증
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 성공", task.exception)
                    if (check_login.isChecked) {
                        MyApplication.prefs.setCheckLogin("checklogin", "google login")
                    }
                    startActivity(MainActivity::class.java)
                } else {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 실패", task.exception)
                    toast("로그인 실패")
                }
            }
    }// firebaseAuthWithGoogle END
}