package com.paandw.poke.view.poke_chat.messaging

import java.io.BufferedReader


class PokeChatListener (private val reader: BufferedReader, private val presenter: PokeChatPresenter) : Thread() {

    var isListening: Boolean = false

    override fun run() {
        while (isListening) {
            try {
                val data = reader.readLine()
                if (data != null) {
                    presenter.messageReceived(data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}