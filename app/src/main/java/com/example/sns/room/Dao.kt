package com.example.sns.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sns.model.ChatModel

@Dao
interface Dao {
    @Query("SELECT * FROM chat where (sender = :sender and receiver = :receiver) or (sender = :receiver and receiver = :sender)")
    fun getMessage(sender : String, receiver: String) : List<ChatDataBase>

    @Insert
    fun insert(chatDataBase: ChatDataBase)

}