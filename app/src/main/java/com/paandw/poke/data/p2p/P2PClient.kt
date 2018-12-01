package com.paandw.poke.data.p2p

import com.paandw.poke.view.poke_chat.messaging.PokeChatListener
import com.paandw.poke.view.poke_chat.messaging.PokeChatPresenter
import java.io.BufferedReader
import java.io.PrintWriter


class P2PClient (private val reader: BufferedReader, private val writer: PrintWriter,
                 private val clientName: String, private val presenter: PokeChatPresenter) {

    private var listener: PokeChatListener = PokeChatListener(reader, presenter)

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