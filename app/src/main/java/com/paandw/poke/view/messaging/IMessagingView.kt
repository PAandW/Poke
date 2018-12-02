package com.paandw.poke.view.messaging

import com.paandw.poke.data.models.Message

interface IMessagingView {
    fun bindMessages(messages: MutableList<Message>, currentUserId: String)
    fun clearMessageText()
}