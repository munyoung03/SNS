package com.example.sns.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentChattingBinding
import com.example.sns.view.activity.MainActivity
import com.example.sns.viewModel.ChattingViewModel
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.Exception

class ChattingFragment : BaseFragment<FragmentChattingBinding, ChattingViewModel>() {


    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_chatting

    override fun init() {
        viewModel.mSocket.on("user connect", onResult)
    }

    override fun observerViewModel() {
        with(viewModel){
            joinRoomBtn.observe(this@ChattingFragment, Observer {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("id", myEmail)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                mSocket.emit("user connect", jsonObject)

            })
        }
    }

    private var onResult:Emitter.Listener= Emitter.Listener { args ->
        activity?.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val success : Boolean
            try {
                success = data.getBoolean("success")
                if(success)
                {
                    startActivity(MainActivity::class.java)
                }
                else{
                    toast("연결에 실패하였습니다.")
                }
            }catch (e: Exception){
                return@Runnable
            }
        })
    }

}