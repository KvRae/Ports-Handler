package dev.app.mobileapplication.APIService

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("http://10.0.2.2:5000/")
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        Log.d("SocketHandler", "Socket getted connected " + mSocket.id())
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
        Log.d("SocketHandler", "Socket setted connected "+mSocket.id())
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}
