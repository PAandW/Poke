package com.paandw.poke.view.chat.p2p

import com.paandw.poke.data.p2p.P2PMessage

interface IP2PChatView {

    fun bindMessages(messages: MutableList<P2PMessage>)

}