package com.paandw.poke.view.group_chat.create

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.data.models.Friend
import com.paandw.poke.data.models.Message
import com.paandw.poke.data.models.User
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class GroupCreationPresenter {

    private lateinit var view: IGroupCreation
    private lateinit var database: DatabaseReference
    private var friends = ArrayList<Friend>()
    private var addedFriendIds = ArrayList<String>()
    private var currentUser: User? = null
    private var setup = true

    fun start(view: IGroupCreation) {
        this.view = view
        database = FirebaseDatabase.getInstance().reference
        val userListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }

            override fun onDataChange(data: DataSnapshot) {
                if (setup) {
                    currentUser = data.getValue(User::class.java)
                    if (currentUser!!.friendsList != null) {
                        friends.addAll(currentUser!!.friendsList)
                    }
                    view.bindFriendList(friends, addedFriendIds)
                    setup = false
                }
            }
        }

        database.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(userListener)
    }

    fun setFriendSelected(id: String, selected: Boolean) {
        if (addedFriendIds.any { it == id } && !selected) {
            addedFriendIds.removeAll { it == id }
        } else if (!addedFriendIds.any { it == id } && selected) {
            addedFriendIds.add(id)
        }

        view.bindFriendList(friends, addedFriendIds)
    }

    fun createGroup(groupName: String) {
        if (addedFriendIds.size == 0) {
            return
        }

        val conversation = Conversation()
        conversation.groupName = groupName
        conversation.userIds = ArrayList<String>()
        conversation.userIds.addAll(addedFriendIds)
        conversation.userIds.add(currentUser!!.id)
        conversation.chatId = UUID.randomUUID().toString()

        database.child("conversations").child(conversation.chatId).runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                view.toGroupChat(conversation.chatId, groupName)
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                val check = data.getValue(Conversation::class.java)
                if (check != null) {
                    return Transaction.success(data)
                }
                data.value = conversation
                return Transaction.success(data)
            }
        })
    }

}