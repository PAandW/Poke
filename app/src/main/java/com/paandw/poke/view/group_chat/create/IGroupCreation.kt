package com.paandw.poke.view.group_chat.create

import com.paandw.poke.data.models.Friend

interface IGroupCreation {
    fun bindFriendList(friendList: MutableList<Friend>, addedFriends: MutableList<String>)
    fun toGroupChat(chatId: String, chatName: String)
}