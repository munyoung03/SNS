package com.example.sns.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentChattingBinding
import com.example.sns.view.activity.ChattingActivity
import com.example.sns.view.activity.MainActivity
import com.example.sns.viewModel.ChattingViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.noFinishStartActivity
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_chatting.*
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
        viewModel.connect()
    }

    override fun observerViewModel() {
        with(viewModel) {

            //버튼 클릭시 emit을 통해 서버에 같은 이벤트 이름을 가진 socket.on으로 jsonObject를 날림
            joinRoomBtn.observe(this@ChattingFragment, {
                Log.d("TAG", "버튼 클릭 성공")
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("id", myEmail.value)
                    Log.d("TAG", myEmail.value.toString())
                } catch (e: JSONException) {
                    Log.d("TAG", "캐치")
                    e.printStackTrace()
                }
                mSocket.emit("user connect", jsonObject)

            })

            finishUserConnect.observe(this@ChattingFragment, Observer {
                Log.d("TAG", it.toString())
                if(it)
                {
                    toast("입장")
                    noFinishStartActivity(ChattingActivity::class.java)
                }
                else
                {
                    toast("실패")
                }
            })
        }
    }
}
