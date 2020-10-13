package com.example.sns.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.sns.base.BaseViewModel
import com.example.sns.widget.SingleLiveEvent
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket

class ChattingViewModel : BaseViewModel(){

    var myEmail = MutableLiveData<String>()
    var targetEmail = MutableLiveData<String>()

    var joinRoomBtn = SingleLiveEvent<Unit>()

    var mSocket: Socket = IO.socket("http://localhost:8080")

    init{
        mSocket.connect()
    }

    fun joinRoomBtnClick()
    {
        joinRoomBtn.call()
    }

}