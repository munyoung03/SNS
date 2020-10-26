package com.example.sns.view.activity

import android.util.Log
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sns.R
import com.example.sns.adapter.ChatAdapter
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

    private lateinit var chatAdapter : ChatAdapter

    override val viewModel: ChattingViewModel
        get() = getViewModel(ChattingViewModel::class)

    override val layoutRes: Int
        get() = R.layout.activity_chatting

    override fun init() {
        viewModel.connect()
        viewModel.chatDb = DataBase.getInstance(this)

        setRecyclerView()
        addData()
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

                chatAdapter.addItem(ChatDataBase(
                    id = 0,
                    message = receiveMessage,
                    receiver = MyApplication.prefs.getUsername("myName", ""),
                    sender = sender,
                    time = receiveDate
                ))

                insertReceiveData()

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

                    chatAdapter.notifyDataSetChanged()

                    insertSendData()

                    messageEdit.value = ""
                } else {
                    toast("전송 실패")
                }
            }
        }
    }

    private fun setRecyclerView()
    {
        chatAdapter = ChatAdapter(viewModel.arrayList)

        chat_recyclerview.adapter = chatAdapter

        chat_recyclerview.layoutManager = LinearLayoutManager(this)
        chat_recyclerview.setHasFixedSize(true)
    }

    private fun addData()
    {
        viewModel.arrayList.clear()

        viewModel.arrayList.addAll(
            viewModel.chatDb?.dao()?.getMessage(
                sender = MyApplication.prefs.getUsername("targetName", ""),
                receiver = MyApplication.prefs.getUsername("myName", "")
            ) as ArrayList<ChatDataBase>
        )

        chatAdapter.notifyDataSetChanged()
    }
}




