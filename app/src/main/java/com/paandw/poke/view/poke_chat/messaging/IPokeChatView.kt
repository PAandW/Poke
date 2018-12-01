package com.paandw.poke.view.poke_chat.messaging

import com.paandw.poke.data.p2p.P2PMessage

interface IPokeChatView {

    fun bindMessages(messages: MutableList<P2PMessage>)

}