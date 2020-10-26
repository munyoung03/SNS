package com.example.sns.view.fragment

import android.content.Context
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
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
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.noFinishStartActivity
import com.example.sns.widget.extension.toast
import kotlinx.android.synthetic.main.fragment_chatting.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.*

class ChattingFragment : BaseFragment<FragmentChattingBinding, ChattingViewModel>() {

    lateinit var roomAdapter: RoomListAdapter
    lateinit var mContext: Context

    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.fragment_chatting

    override fun init() {
        viewModel.chatDb = DataBase.getInstance(mContext)

        setRecyclerView()

        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("TAG", "버튼 클릭")
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onResume() {
        super.onResume()
        viewModel.socketReset()
        viewModel.setFragmentRecyclerViewData()
        setAdapter()
    }

    override fun observerViewModel() {
        with(viewModel) {
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

    private fun setRecyclerView()
    {
        chat_room_recyclerview.layoutManager = LinearLayoutManager(mContext)
        chat_room_recyclerview.setHasFixedSize(true)
    }


    private fun setAdapter()
    {
        roomAdapter = RoomListAdapter(viewModel.arrayList) { item: ChatDataBase ->
            viewModel.tryRoomConnect(
                item
            )
        }

        chat_room_recyclerview.adapter = roomAdapter
    }
}
