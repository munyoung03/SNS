package com.example.sns.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentMypageBinding
import com.example.sns.view.activity.LoginActivity
import com.example.sns.viewModel.MyPageViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_mypage.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageViewModel>() {

    companion object {
        private val IMAGE_PICK_CODE = 1000;
    }

    override val viewModel: MyPageViewModel
        get() = getViewModel(MyPageViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_mypage

    override fun init() {
        binding.emailValue.text = MyApplication.prefs.getEmail("email", "no email")
        binding.username.text = MyApplication.prefs.getUsername("name", "no username")
    }

    override fun observerViewModel() {
        with(viewModel){
            logoutBtn.observe(this@MyPageFragment, Observer {
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                setData()
                startActivity(LoginActivity::class.java)
            })
            image.observe(this@MyPageFragment, Observer {
                pickImageFromGallery()
            })
        }
    }

    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageView.setImageURI(data?.data)
        }
    }
}

