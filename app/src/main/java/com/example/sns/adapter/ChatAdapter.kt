package com.example.sns.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.sns.R
import com.example.sns.model.ChatModel
import com.example.sns.widget.MyApplication
import org.w3c.dom.Text
import kotlin.collections.ArrayList

class ChatAdapter(var arrayList: ArrayList<ChatModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    fun addItem(item: ChatModel) {//아이템 추가
        arrayList.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if(viewType == 1){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_chat, parent, false)
            return Holder(view)
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_your_chat, parent, false)
            return Holder2(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.chat_Text.text = arrayList[position].message
        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if(holder is Holder2) {
            holder.chat_Text.text = arrayList[position].message
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        //친구목록 모델의 변수들 정의하는부분
        val chat_Text = itemView.findViewById<TextView>(R.id.chat_Text)
    }

    //상대가친 채팅 뷰홀더
    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chat_Text = itemView.findViewById<TextView>(R.id.chat_Text)
    }

    override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음

        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (arrayList[position].name == MyApplication.prefs.getUsername("myName","")) {
            1
        } else {
            2
        }
    }
}