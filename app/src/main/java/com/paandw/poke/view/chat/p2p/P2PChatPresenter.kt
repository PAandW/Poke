package com.paandw.poke.view.chat.p2p

import android.net.wifi.p2p.WifiP2pInfo
import com.paandw.poke.data.p2p.P2PClient
import com.paandw.poke.data.p2p.P2PMessage
import com.paandw.poke.data.service.ConnectToP2PHost
import com.paandw.poke.data.service.CreateP2PHost
import com.paandw.poke.data.service.SendMessage
import java.io.PrintWriter
import java.net.ServerSocket
import java.text.SimpleDateFormat
import java.util.*

class P2PChatPresenter {

    private lateinit var info: WifiP2pInfo
    private lateinit var view: IP2PChatView
    private var client: P2PClient? = null
    private var writerToHost: PrintWriter? = null

    private val messages = ArrayList<P2PMessage>()

    fun start(info: WifiP2pInfo, view: IP2PChatView) {
        this.info = info
        this.view = view

        try {
            if (info.isGroupOwner) {
                val serverSocket = ServerSocket(8080)
                CreateP2PHost(this, serverSocket).execute()
            } else {
                ConnectToP2PHost(this, info).execute()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setClient(client: P2PClient) {
        this.client = client
    }

    fun setWriterToHost(writer: PrintWriter) {
        writerToHost = writer
    }

    fun messageReceived(jsonString: String) {
        val message = P2PMessage(jsonString)
        message.isMine = false
        messages.add(message)

        view.bindMessages(messages)
    }

    fun sendMessage(message: String) {
        val dateFormat = SimpleDateFormat("MM/dd/yy hh:mm a", Locale.US)
        val timeString = dateFormat.format(Date())

        val p2pMessage = P2PMessage(message, timeString, true)

        messages.add(p2pMessage)
        view.bindMessages(messages)

        SendMessage(this, client, writerToHost).execute(p2pMessage)
    }

}