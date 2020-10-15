package com.example.sns.network.socket

import com.example.sns.model.ChatModel
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONObject

object SocketManager {
    private var socket: Socket? = null

    private var observers: ArrayList<SocketListeners> = arrayListOf()
    fun getSocket(): Socket {
        synchronized(this) {
            if (socket == null) {
                socket = IO.socket("http://192.168.10.163:8080");
                socket!!.on(Socket.EVENT_CONNECT) {
                    for (observer in observers) {
                        observer.onConnect()
                    }
                }
                socket!!.on("send message") { it ->
                    val data = it[0] as JSONObject
                    val success: Boolean
                    success = data.getBoolean("success")
                    observers.forEach { it.onUserSendMessage(success) }
                }
                socket!!.on("user connect") {

                    val data = it[0] as JSONObject
                    val success: Boolean
                    success = data.getBoolean("success")
                    observers.forEach { it.onUserConnect(success) }
                }
                socket!!.on("message") { args ->
                    val data = args[0] as JSONObject

                    val receiveDate = data.getString("when")
                    val receiveMessage = data.getString("message")
                    val receiveUser = data.getString("user")
                    observers.forEach {
                        it.onMessageReceive(ChatModel(receiveMessage, receiveUser))
                    }
                }
                socket!!.on(Socket.EVENT_DISCONNECT) {
                    for (observer in observers) {
                        observer.onDisconnect()
                    }
                }
                socket!!.connect()
            }
            return socket!!;
        }
    }
    fun observe(listener : SocketListeners) {
        if(!observers.contains(listener))
            observers.add(listener)
    }
}