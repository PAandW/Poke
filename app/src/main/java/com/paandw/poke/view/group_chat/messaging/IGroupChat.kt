package com.paandw.poke.view.group_chat.messaging

import com.paandw.poke.data.models.Message

interface IGroupChat {
    fun bindMessages(messages: MutableList<Message>, userId: String)
    fun clearMessageText()
}