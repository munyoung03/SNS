package com.example.sns.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns.R
import com.example.sns.adapter.ChatAdapter
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityChattingBinding
import com.example.sns.model.ChatModel
import com.example.sns.viewModel.ChattingViewModel
import com.example.sns.viewModel.LoginViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.startActivity
import com.example.sns.widget.extension.toast
import com.github.nkzawa.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.android.synthetic.main.fragment_chatting.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.Exception

class ChattingActivity : BaseActivity<ActivityChattingBinding, ChattingViewModel>() {

    var arrayList = arrayListOf<ChatModel>()
    val mAdapter = ChatAdapter(arrayList)

    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.activity_chatting

    override fun init() {
        chat_recyclerview.adapter = mAdapter
        //레이아웃 매니저 선언
        chat_recyclerview.layoutManager = LinearLayoutManager(this)
        chat_recyclerview.setHasFixedSize(true)//아이템이 추가삭제될때 크기측면에서 오류 안나게 해줌

        viewModel.mSocket.on("message", newMessage)
        viewModel.mSocket.on("send message", sendMessageStatus)
    }

    override fun observerViewModel() {
        with(viewModel){
            sendMessageBtn.observe(this@ChattingActivity, Observer {
                sendMessage()
            })
        }
    }

    private var sendMessageStatus: Emitter.Listener = Emitter.Listener { args ->
        Log.d("TAG", "안녕하세연")
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val success : Boolean
            val message : String
            try {
                success = data.getBoolean("success")
                if(success)
                {
                    val format = ChatModel(editText2.text.toString(), MyApplication.prefs.getUsername("myName", ""))
                    mAdapter.addItem(format)
                    mAdapter.notifyDataSetChanged()

                    editText2.setText("")
                    toast("전송성공")
                }
                else{
                    toast("전송실패")
                }
            }catch (e: Exception){
                return@Runnable
            }
        })
    }

    private var newMessage: Emitter.Listener = Emitter.Listener { args ->
        Log.d("TAG", "잘가세연")
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val user : String
            val message : String
            val date : String
            try {
                date = data.getString("when")
                message = data.getString("message")
                user = data.getString("user")

                Log.d("TAG", "user : $user\n message : $message")

                val format = ChatModel(message, user)

                mAdapter.addItem(format)
                mAdapter.notifyDataSetChanged()
            }catch (e: Exception){
                Log.d("TAG", "오류세연")
                return@Runnable
            }
        })
    }

    private fun sendMessage() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("room", MyApplication.prefs.getUsername("tartgetName", ""))
            jsonObject.put("message", editText2.text.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        viewModel.mSocket.emit("message", jsonObject)
    }
}