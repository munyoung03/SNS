package com.example.sns.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
class ChatDataBase (
    @PrimaryKey(autoGenerate = true) var id: Long,
)