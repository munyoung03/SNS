package com.example.sns.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.model.ChatModel
import com.example.sns.network.socket.SocketListeners
import com.example.sns.network.socket.SocketManager
import com.example.sns.room.ChatDataBase
import com.example.sns.room.DataBase
import com.example.sns.widget.MyApplication
import com.example.sns.widget.SingleLiveEvent
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class ChattingViewModel : BaseViewModel(), SocketListeners {

    var messageEdit = MutableLiveData<String>()

    var sender: String = ""
    var receiveMessage: String = ""
    var receiveDate: Long = 0

    val userName: String by lazy { MyApplication.prefs.getUsername("myName", "") }
    var targetName: String = ""

    var finishSend = MutableLiveData<Boolean>()
    var finishUserConnect = MutableLiveData<Boolean>()
    var finishReceiveMessage = MutableLiveData<Boolean>()

    var sendMessageBtn = SingleLiveEvent<Unit>()

    var mSocket: Socket? = null

    var arrayList = arrayListOf<ChatDataBase>()

    var chatDb: DataBase? = null

    fun connect() {
        Log.d("TAG", "connect")
        mSocket = SocketManager.getSocket()
        SocketManager.connectSocket()
        SocketManager.observe(this)
    }

    fun sendMessageBtnClick() {
        sendMessageBtn.call()
    }

    fun sendMessage() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("room", targetName)
            jsonObject.put("message", messageEdit.value.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mSocket?.emit("message", jsonObject)
    }

    fun tryRoomConnect(item: ChatDataBase) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", userName)
            targetName = item.receiver
            Log.d("TAG", targetName)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mSocket?.emit("user connect", jsonObject)
    }

    override fun onMessageReceive(model: ChatModel) {

        sender = model.name
        sender = sender.substring(0, sender.length - 8)
        receiveMessage = model.message
        receiveDate = model.date
        finishReceiveMessage.value = true

        chatDb?.dao()?.insert(
            ChatDataBase(
                id = 0,
                message = receiveMessage,
                receiver = userName,
                sender = sender,
                time = receiveDate
            )
        )
    }

    override fun onConnect() {
        Log.d("TAG", "Connect!")
    }

    override fun onDisconnect() {
        Log.d("TAG", "Disconnect!")
    }

    override fun onUserConnect(success: Boolean) {
        finishUserConnect.value = success
    }

    override fun onUserSendMessage(success: Boolean) {
        finishSend.value = success
    }

    fun insertReceiveData() {
        chatDb?.dao()?.insert(
            ChatDataBase(
                id = 0,
                message = receiveMessage,
                receiver = userName,
                sender = sender,
                time = receiveDate
            )
        )
    }

    fun insertSendData() {
        chatDb?.dao()?.insert(
            ChatDataBase(
                id = 0,
                message = messageEdit.value.toString().trim(),
                receiver = targetName,
                sender = userName,
                time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
        )
    }

    fun socketReset() {
        SocketManager.closeSocket()
        connect()
    }

    fun setFragmentRecyclerViewData() {
        arrayList.clear()

        arrayList.addAll(
            chatDb?.dao()?.getRecentMessage(userName)!! as ArrayList<ChatDataBase>
        )

    }
}
