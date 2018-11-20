package com.paandw.poke.data.service

import android.content.Context
import android.net.wifi.p2p.WifiP2pInfo
import android.os.AsyncTask
import com.paandw.poke.view.chat.p2p.lobby.P2PLobbyPresenter
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket


class P2PServer(private val presenter: P2PLobbyPresenter) : AsyncTask<Void, Void, String?>(){

    override fun doInBackground(vararg p0: Void?): String? {

        //Creates server socket
        val socket = ServerSocket(8080)
        return socket.use {
            //Here it'll wait for a client connection
            val client = socket.accept()

            //At this point the other device has connected and is sending a message
            val inputStream = client.getInputStream()
            socket.close()
            inputStream.toString()
        }

    }

    override fun onPostExecute(result: String?) {
        result?.run {
            presenter.messageReceived(this)
        }
    }
}

class P2PClient(private val context: Context, private val info: WifiP2pInfo) : AsyncTask<String?, Void, Void>() {

    override fun doInBackground(vararg message: String?): Void? {
        val socket = Socket()
        socket.bind(null)
        socket.connect((InetSocketAddress(info.groupOwnerAddress, 8080)), 500)
        val outputStream = socket.getOutputStream()
        outputStream.write(message[0]!!.toByteArray())
        outputStream.close()

        socket.takeIf { it.isConnected } ?.apply {
            close()
        }
        return null
    }
}