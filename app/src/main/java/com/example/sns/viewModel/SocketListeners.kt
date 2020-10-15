package com.example.sns.viewModel

import com.example.sns.model.ChatModel

interface SocketListeners {
    fun onMessageReceive(model : ChatModel);
    fun onConnect();
    fun onDisconnect();
    fun onUserConnect(success : Boolean);
    fun onUserSendMessage(success: Boolean)
}