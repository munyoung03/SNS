package com.example.sns.view.fragment

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.sns.R
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentChattingBinding
import com.example.sns.network.socket.SocketManager
import com.example.sns.view.activity.ChattingActivity
import com.example.sns.viewModel.ChattingViewModel
import com.example.sns.widget.extension.noFinishStartActivity
import com.example.sns.widget.extension.toast
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ChattingFragment : BaseFragment<FragmentChattingBinding, ChattingViewModel>() {


    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_chatting

    override fun init() {
    }

    override fun onPause() {
        Log.d("TAG", "Pause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "Resume")
        SocketManager.closeSocket()
        viewModel.connect()
    }

    override fun observerViewModel() {
        with(viewModel) {

            //버튼 클릭시 emit을 통해 서버에 같은 이벤트 이름을 가진 socket.on으로 jsonObject를 날림
            joinRoomBtn.observe(this@ChattingFragment) {
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

            finishUserConnect.observe(this@ChattingFragment, Observer {
                Log.d("TAG", it.toString())
                if (it) {
                    toast("입장")
                    noFinishStartActivity(ChattingActivity::class.java)
                } else {
                    toast("실패")
                }
            })
        }
    }
}
