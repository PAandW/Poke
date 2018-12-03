package com.paandw.poke.view.group_chat

import com.paandw.poke.data.models.Conversation

interface IGroupChatList {
    fun bindGroupChatList(conversations: MutableList<Conversation>)
    fun toGroupChat(conversation: Conversation)
}