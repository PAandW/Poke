package com.paandw.poke.view.friends

import com.paandw.poke.data.models.Friend
import com.paandw.poke.data.models.User

interface IFriendsView {
    fun bindUserSearchItems(users: ArrayList<String>)
    fun bindFriendList(friends: MutableList<Friend>?)
    fun toPrivateMessaging(privateMessageId: String, recipientName: String)
}