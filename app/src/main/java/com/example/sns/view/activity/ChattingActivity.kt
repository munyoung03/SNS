package com.example.sns.view.activity

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns.R
import com.example.sns.adapter.ChatAdapter
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityChattingBinding
import com.example.sns.model.ChatModel
import com.example.sns.viewModel.ChattingViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.toast
import kotlinx.android.synthetic.main.activity_chatting.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ChattingActivity : BaseActivity<ActivityChattingBinding, ChattingViewModel>() {

    var arrayList = arrayListOf<ChatModel>()
    val mAdapter = ChatAdapter(arrayList)

    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.activity_chatting

    override fun init() {
        viewModel.connect()
        chat_recyclerview.adapter = mAdapter
        //레이아웃 매니저 선언
        chat_recyclerview.layoutManager = LinearLayoutManager(this)
        chat_recyclerview.setHasFixedSize(true)//아이템이 추가삭제될때 크기측면에서 오류 안나게 해줌

    }

    override fun observerViewModel() {
        with(viewModel) {
            sendMessageBtn.observe(this@ChattingActivity, {
                sendMessage()
            })

            finishReceiveMessage.observe(this@ChattingActivity, {
                Log.d("TAG", "SUC")
                mAdapter.addItem(ChatModel(receiveMessage, receiveUser))
                mAdapter.notifyDataSetChanged()
            })

            finishSend.observe(this@ChattingActivity, {
                if (it) {
                    toast("전송성공")
                    mAdapter.addItem(
                        ChatModel(
                            messageEdit.value.toString(),
                            MyApplication.prefs.getUsername("myName", "")
                        )
                    )
                    mAdapter.notifyDataSetChanged()
                    editText2.setText("")
                } else {
                    toast("전송 실패")
                }
            })
        }
    }
}




