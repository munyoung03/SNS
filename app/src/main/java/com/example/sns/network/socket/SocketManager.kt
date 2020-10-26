package com.example.sns.network.socket

import android.util.Log
import com.example.sns.model.ChatModel
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

object SocketManager {
    private var socket: Socket? = null

    private var observers: ArrayList<SocketListeners> = arrayListOf()
    fun getSocket(): Socket {
        synchronized(SocketManager::class) {
            Log.d("TAG", socket.toString())
            if (socket == null) {
                Log.d("TAG", "asdf")
                socket = IO.socket("http://10.80.161.39:8080")
                socket!!.on(Socket.EVENT_CONNECT) {
                    for (observer in observers) {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main)
                            {
                                observer.onConnect()
                            }
                        }
                    }
                }
                socket!!.on("send message") { it ->
                    val data = it[0] as JSONObject
                    val success: Boolean
                    success = data.getBoolean("success")
                    observers.forEach {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main)
                            {
                                it.onUserSendMessage(success)
                            }
                        }
                    }
                }
                socket!!.on("user connect") {

                    val data = it[0] as JSONObject
                    val success: Boolean
                    success = data.getBoolean("success")
                    observers.forEach {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main)
                            {
                                it.onUserConnect(success)
                            }
                        }
                    }
                }
                socket!!.on("message") { args ->
                    val data = args[0] as JSONObject

                    val receiveDate = data.getLong("when")
                    val receiveMessage = data.getString("message")
                    val sender = data.getString("user")
                    observers.forEach {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main)
                            {
                                it.onMessageReceive(ChatModel(receiveMessage, sender, receiveDate))
                            }
                        }
                    }
                }
                socket!!.on(Socket.EVENT_DISCONNECT) {
                    for (observer in observers) {
                        observer.onDisconnect()
                    }
                }
                socket!!.connect()
            }
            return socket!!
        }
    }

    fun observe(listener: SocketListeners) {
        if (!observers.contains(listener))
            observers.add(listener)
    }

    fun closeSocket() {
        synchronized(SocketManager::class) {
            socket?.disconnect()
            Log.d("TAG", "socket : " + socket.toString())
            observers.clear()
        }
    }

    fun connectSocket() {
        socket?.connect()
    }
}