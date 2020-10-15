package com.example.sns.viewModel

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.SingleLiveEvent
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class ChattingViewModel : BaseViewModel(){

    var myEmail = MutableLiveData<String>()
    var targetEmail = MutableLiveData<String>()
    var messageEdit = MutableLiveData<String>()

    var receiveUser: String = ""
    var receiveMessage: String = ""
    var receiveDate: String = ""

    var finishSend = MutableLiveData<Boolean>()
    var finishUserConnect = MutableLiveData<Boolean>()
    var finishReceiveMessage = MutableLiveData<Boolean>()

    var joinRoomBtn = SingleLiveEvent<Unit>()
    var sendMessageBtn = SingleLiveEvent<Unit>()

    var mSocket: Socket = IO.socket("http://192.168.10.163:8080")

    init{
        if(!mSocket.connected()) {
            mSocket.on("send message") { args ->
                Log.d("TAG", "안녕하세연")
                val data = args[0] as JSONObject
                val success: Boolean
                val message: String
                try {
                    success = data.getBoolean("success")
                    if (success) {
                        finishSend.value = true

                        messageEdit.value = ""
                    } else {
                        finishSend.value = false
                    }
                } catch (e: Exception) {
                }
            }

            mSocket.on("user connect") { args ->
                val data = args[0] as JSONObject
                val success: Boolean
                try {
                    success = data.getBoolean("success")
                    if (success) {
                        Log.d("TAG", "룸입장 성공")
                        MyApplication.prefs.setUsername("myName", myEmail.value.toString())
                        MyApplication.prefs.setUsername("targetName", targetEmail.value.toString())
                        finishUserConnect.postValue(true)
                        Log.d("TAG", finishUserConnect.value.toString())
                    } else {
                        finishUserConnect.postValue(false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        mSocket.on("message", object : Emitter.Listener{
            override fun call(vararg args: Any?) {
                print(args[0])
            }
        })
        mSocket.on("message") { args ->
            Log.d("TAG", "잘가세연")
            val data = args[0] as JSONObject
            try {
                receiveDate = data.getString("when")
                receiveMessage = data.getString("message")
                receiveUser = data.getString("user")

                Log.d("TAG", "user : $receiveUser\n message : $receiveMessage")

                finishReceiveMessage.value = true
            } catch (e: Exception) {
                finishReceiveMessage.value = false
            }
        }
        mSocket.connect()
        Log.d("TAG", "connect 성공")
    }


    fun joinRoomBtnClick()
    {
        joinRoomBtn.call()
    }

    fun sendMessageBtnClick()
    {
        sendMessageBtn.call()
    }

    fun sendMessage() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("room", MyApplication.prefs.getUsername("tartgetName", ""))
            jsonObject.put("message", messageEdit.value.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mSocket.emit("message", jsonObject)
    }
}