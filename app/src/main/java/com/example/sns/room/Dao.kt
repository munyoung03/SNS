package com.example.sns.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sns.model.ChatModel

@Dao
interface Dao {
    @Query("SELECT * FROM chat where sender = :sender or receiver = :receiver and sender = :receiver or receiver = :sender")
    fun getMessage(sender : String, receiver: String) : ArrayList<ChatDataBase>

    @Insert
    fun insert(chatDataBase: ChatDataBase)

    @Query("DROP TABLE chat")
    fun delete()
}