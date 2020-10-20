package com.example.sns.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Query("SELECT * FROM chat")
    fun getAll() : List<ChatDataBase>

    @Insert
    fun insert(chatDataBase: ChatDataBase)

    @Query("DROP TABLE chat")
    fun delete()
}