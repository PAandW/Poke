package com.paandw.poke.view.chat.p2p

import java.io.BufferedReader


class P2PChatListener (private val reader: BufferedReader, private val presenter: P2PChatPresenter) : Thread() {

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