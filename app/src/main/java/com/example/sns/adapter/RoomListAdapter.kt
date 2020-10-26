package com.example.sns.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sns.R
import com.example.sns.room.ChatDataBase
import com.example.sns.widget.MyApplication

class RoomListAdapter(var chatUserList: ArrayList<ChatDataBase>, private val handler: (ChatDataBase) -> Unit) : RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

    fun addItem(item: ChatDataBase) {//아이템 추가
        chatUserList.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_room_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatUserList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatUserList[position], handler)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userName = itemView.findViewById<TextView>(R.id.chat_room_name)
        val lastMessage = itemView.findViewById<TextView>(R.id.chat_room_message)

        fun bind(item : ChatDataBase, handler : (ChatDataBase) -> Unit)
        {
            userName.text = item.receiver
            lastMessage.text = item.message

            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "눌림", Toast.LENGTH_SHORT).show()
                handler(item)
            }
        }
    }

}