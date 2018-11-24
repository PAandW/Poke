package com.paandw.poke.data.service

import android.net.wifi.p2p.WifiP2pInfo
import android.os.AsyncTask
import com.paandw.poke.data.p2p.P2PClient
import com.paandw.poke.data.p2p.P2PMessage
import com.paandw.poke.view.chat.p2p.P2PChatPresenter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket


class CreateP2PHost(private val presenter: P2PChatPresenter, private val serverSocket: ServerSocket) : AsyncTask<Void, Void, Void>(){
    override fun doInBackground(vararg void: Void?): Void? {
        try {
            val socket = serverSocket.accept()
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val writer = PrintWriter(socket.getOutputStream(), true)
            val clientName = reader.readLine()
            val client = P2PClient(reader, writer, clientName, presenter)

            presenter.setClient(client)
            client.startListening()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}

class ConnectToP2PHost(private val presenter: P2PChatPresenter, private val info: WifiP2pInfo) : AsyncTask<Void, Void, Void>() {

    private var reader: BufferedReader? = null

    override fun doInBackground(vararg void: Void?): Void? {
        val host = info.groupOwnerAddress
        val socket = Socket()

        try {
            socket.connect(InetSocketAddress(host.hostAddress, 8080), 500)
            reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val writer = PrintWriter(socket.getOutputStream(), true)

            presenter.setWriterToHost(writer)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        BeginListeningToHost(presenter, reader!!).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        presenter.sendInitialSlug()
    }
}

class BeginListeningToHost(private val presenter: P2PChatPresenter, private val reader: BufferedReader) : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg void: Void?): Void? {
        try {
            while(true) {
                val data = reader.readLine()
                if (data != null) {
                    presenter.messageReceived(data)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}

class SendMessage(private var client: P2PClient?, private var writerToHost: PrintWriter?) : AsyncTask<P2PMessage, Void, Void>() {

    override fun doInBackground(vararg message: P2PMessage): Void? {

        if (client != null) {
            val writer = client?.getWriter()
            writer?.println(message[0].asJsonString)
        } else {
            writerToHost?.println(message[0].asJsonString)
        }

        return null
    }
}