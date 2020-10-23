package com.example.sns.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.model.ChatModel
import com.example.sns.network.socket.SocketListeners
import com.example.sns.network.socket.SocketManager
import com.example.sns.widget.MyApplication
import com.example.sns.widget.SingleLiveEvent
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONException
import org.json.JSONObject

class ChattingViewModel() : BaseViewModel(), SocketListeners {

    var myEmail = MutableLiveData<String>()
    var targetEmail = MutableLiveData<String>()
    var messageEdit = MutableLiveData<String>()

    var sender: String = ""
    var receiveMessage: String = ""
    var receiveDate: Long = 0

    var finishSend = MutableLiveData<Boolean>()
    var finishUserConnect = MutableLiveData<Boolean>()
    var finishReceiveMessage = MutableLiveData<Boolean>()

    var joinRoomBtn = SingleLiveEvent<Unit>()
    var sendMessageBtn = SingleLiveEvent<Unit>()
    val itemList = MutableLiveData<ChatModel>()

    var mSocket: Socket? = null

    fun connect() {
        Log.d("TAG", "connect")
        mSocket = SocketManager.getSocket()
        SocketManager.connectSocket()
        SocketManager.observe( this)


    }


    fun joinRoomBtnClick() {
        joinRoomBtn.call()
    }

    fun sendMessageBtnClick() {
        sendMessageBtn.call()
    }

    fun sendMessage() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("room", MyApplication.prefs.getUsername("targetName", ""))
            jsonObject.put("message", messageEdit.value.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mSocket?.emit("message", jsonObject)
    }

    fun tryRoomConnect(){
        Log.d("TAG", "버튼 클릭 성공")
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", myEmail.value)
            Log.d("TAG", myEmail.value.toString())
        } catch (e: JSONException) {
            Log.d("TAG", "캐치")
            e.printStackTrace()
        }
        mSocket?.emit("user connect", jsonObject)
    }

    override fun onMessageReceive(model: ChatModel) {

        itemList.value = model;
        sender = model.name
        sender = sender.substring(0, sender.length - 8)
        receiveMessage = model.message
        receiveDate = model.date
        finishReceiveMessage.value = true
        Log.d("TAG", "user : $sender\n message : $receiveMessage")

    }

    override fun onConnect() {
        Log.d("TAG", "Connect!")
    }

    override fun onDisconnect() {
        Log.d("TAG", "Disconnect!")
    }

    override fun onUserConnect(success: Boolean) {
        if (success) {
            Log.d("TAG", "룸입장 성공")
            MyApplication.prefs.setUsername("myName", myEmail.value.toString())
            MyApplication.prefs.setUsername("targetName", targetEmail.value.toString())
            Log.d("TAG", targetEmail.value.toString())
            finishUserConnect.value = true
        } else {
            finishUserConnect.value = false
        }
    }

    override fun onUserSendMessage(success: Boolean) {
        if (success) {
            Log.d("TAG", "들어옴2")
            finishSend.value = true
            Log.d("TAG", "전송 : ${finishSend.value.toString()}")
        } else {
            Log.d("TAG", "들어옴3")
            finishSend.value = false
        }
    }

}

/*

    private var sendMessageResponse = Emitter.Listener { args->
        Log.d("TAG", "들어옴")
        val data = args[0] as JSONObject
        val success: Boolean
        val message: String
        try {
            success = data.getBoolean("success")
            if (success) {
                Log.d("TAG", "들어옴2")
                GlobalScope.launch {
                    withContext(Dispatchers.Main){
                        finishSend.value = true
                        Log.d("TAG", "전송 : ${finishSend.value.toString()}")
                    }
                }
            } else {
                Log.d("TAG", "들어옴3")
                GlobalScope.launch {
                    withContext(Dispatchers.Main){
                        finishSend.value = false
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("TAG", "들어옴4")
            Log.d("TAG", "캐치 : $e")
        }
    }

    private var newUser = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val success: Boolean
        try {
            success = data.getBoolean("success")
            if (success) {
                Log.d("TAG", "룸입장 성공")
                MyApplication.prefs.setUsername("myName", myEmail.value.toString())
                MyApplication.prefs.setUsername("targetName", targetEmail.value.toString())
                GlobalScope.launch{
                    withContext(Dispatchers.Main){
                        finishUserConnect.value = true
                        Log.d("TAG", "room : ${finishUserConnect.value.toString()}")
                    }
                }

            } else {
                finishUserConnect.postValue(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var newMessage = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        try {
            receiveDate = data.getString("when")
            receiveMessage = data.getString("message")
            receiveUser = data.getString("user")

            receiveUser = receiveUser.substring(0, receiveUser.length - 8)

        } catch (e: Exception) {
            finishReceiveMessage.postValue(false)
            e.printStackTrace()
        }
    }
 */