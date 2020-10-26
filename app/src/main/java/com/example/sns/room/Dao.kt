package com.example.sns.room


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Query("SELECT * FROM chat where (sender = :sender and receiver = :receiver) or (sender = :receiver and receiver = :sender)")
    fun getMessage(sender: String, receiver: String): List<ChatDataBase>

    @Query("select * from ( SELECT id, message, case WHEN sender == :myName THEN sender ELSE receiver END as sender, case WHEN sender == :myName THEN receiver ELSE sender END as receiver, time from chat where sender == :myName or receiver == :myName order by id DESC)tab group by receiver, sender")
    fun getRecentMessage(myName: String): List<ChatDataBase>

    @Insert
    fun insert(chatDataBase: ChatDataBase)
}