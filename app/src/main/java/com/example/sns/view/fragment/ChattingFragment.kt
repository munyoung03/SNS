package com.example.sns.view.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns.R
import com.example.sns.adapter.RoomListAdapter
import com.example.sns.base.BaseFragment
import com.example.sns.databinding.FragmentChattingBinding
import com.example.sns.network.socket.SocketManager
import com.example.sns.room.ChatDataBase
import com.example.sns.room.DataBase
import com.example.sns.view.activity.ChattingActivity
import com.example.sns.viewModel.ChattingViewModel
import com.example.sns.widget.extension.noFinishStartActivity
import com.example.sns.widget.extension.toast
import kotlinx.android.synthetic.main.fragment_chatting.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.ArrayList

class ChattingFragment : BaseFragment<FragmentChattingBinding, ChattingViewModel>() {

    lateinit var mContext : Context

    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_chatting

    override fun init() {
        viewModel.chatDb = DataBase.getInstance(mContext)


        chat_room_recyclerview.adapter = viewModel.roomAdapter
        //레이아웃 매니저 선언
        chat_room_recyclerview.layoutManager = LinearLayoutManager(mContext)
        chat_room_recyclerview.setHasFixedSize(true)//아이템이 추가삭제될때 크기측면에서 오류 안나게 해줌]

        viewModel.arrayList.clear()

        viewModel.chatDb?.dao()?.getRecentMessage("a")?.forEach {
            Log.d("TAG", "${it.receiver}, ${it.message}")
        }

        viewModel.arrayList.addAll(
            viewModel.chatDb?.dao()?.getRecentMessage("a")!! as ArrayList<ChatDataBase>
        )

        viewModel.roomAdapter = RoomListAdapter(viewModel.arrayList)
        viewModel.roomAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
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
