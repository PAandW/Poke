package com.paandw.poke.data.p2p

import com.paandw.poke.view.chat.p2p.P2PChatListener
import com.paandw.poke.view.chat.p2p.P2PChatPresenter
import java.io.BufferedReader
import java.io.PrintWriter


class P2PClient (private val reader: BufferedReader, private val writer: PrintWriter,
                 private val clientName: String, private val presenter: P2PChatPresenter) {

    private var listener: P2PChatListener = P2PChatListener(reader, presenter)

    fun getClientName() : String = clientName

    fun getReader() : BufferedReader = reader

    fun getWriter() : PrintWriter = writer

    fun startListening() {
        listener.isListening = true
        listener.start()
    }

    fun stopListening() {
        listener.isListening = false
    }

}