package com.example.sns.view.activity

import android.util.Log
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns.R
import com.example.sns.base.BaseActivity
import com.example.sns.databinding.ActivityChattingBinding
import com.example.sns.room.ChatDataBase
import com.example.sns.room.DataBase
import com.example.sns.viewModel.ChattingViewModel
import com.example.sns.widget.MyApplication
import com.example.sns.widget.extension.toast
import kotlinx.android.synthetic.main.activity_chatting.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChattingActivity : BaseActivity<ActivityChattingBinding, ChattingViewModel>() {



    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.activity_chatting

    override fun init() {
        viewModel.connect()
        viewModel.chatDb = DataBase.getInstance(this)
        chat_recyclerview.adapter = viewModel.chatAdapter
        //레이아웃 매니저 선언
        chat_recyclerview.layoutManager = LinearLayoutManager(this)
        chat_recyclerview.setHasFixedSize(true)//아이템이 추가삭제될때 크기측면에서 오류 안나게 해줌]

        viewModel.arrayList.clear()

        viewModel.arrayList.addAll(
            viewModel.chatDb?.dao()?.getMessage(
                sender = MyApplication.prefs.getUsername("targetName", ""),
                receiver = MyApplication.prefs.getUsername("myName", "")
            ) as ArrayList<ChatDataBase>
        )
        viewModel.chatDb?.dao()?.getRecentMessage(MyApplication.prefs.getUsername("myName", ""))?.forEach {
            Log.d("TAG", "${it.receiver}, ${it.message}")
        }
        viewModel.chatAdapter.notifyDataSetChanged()

    }

    override fun observerViewModel() {
        with(viewModel) {
            sendMessageBtn.observe(this@ChattingActivity) {
                if(editText2.text.isNotEmpty())
                {
                    sendMessage()
                }

            }

            finishReceiveMessage.observe(this@ChattingActivity) {
                Log.d("TAG", "SUC")
                chatAdapter.addItem(ChatDataBase(
                    id = 0,
                    message = receiveMessage,
                    receiver = MyApplication.prefs.getUsername("myName", ""),
                    sender = sender,
                    time = receiveDate
                ))

                chatDb?.dao()?.insert(ChatDataBase(
                        id = 0,
                        message = receiveMessage,
                        receiver = MyApplication.prefs.getUsername("myName", ""),
                        sender = sender,
                        time = receiveDate
                ))

                chatAdapter.notifyDataSetChanged()
            }

            finishSend.observe(this@ChattingActivity) {
                if (it) {
                    toast("전송성공")
                    chatAdapter.addItem(ChatDataBase(
                                id = 0,
                                message = editText2.text.toString().trim(),
                                receiver = MyApplication.prefs.getUsername("targetName", ""),
                                sender = MyApplication.prefs.getUsername("myName", ""),
                                time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                            ))

                    chatDb?.dao()?.insert(ChatDataBase(
                        id = 0,
                        message = editText2.text.toString().trim(),
                        receiver = MyApplication.prefs.getUsername("targetName", ""),
                        sender = MyApplication.prefs.getUsername("myName", ""),
                        time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                    ))
                    
                    Log.d("TAG", "receiver : ${MyApplication.prefs.getUsername("targetName", "")}")
                    Log.d("TAG", "sender : ${MyApplication.prefs.getUsername("myName", "")}")

                    chatAdapter.notifyDataSetChanged()
                    editText2.setText("")
                } else {
                    toast("전송 실패")
                }
            }
        }
    }
}




