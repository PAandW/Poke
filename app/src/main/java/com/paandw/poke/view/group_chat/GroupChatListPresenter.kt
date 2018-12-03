package com.paandw.poke.view.group_chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.data.models.User

class GroupChatListPresenter {

    private lateinit var view: IGroupChatList
    private lateinit var database: DatabaseReference
    private var currentUser: User? = null
    private var isSettingUp = true
    private var groupChatList = ArrayList<Conversation>()

    fun start(view: IGroupChatList) {
        this.view = view
        database = FirebaseDatabase.getInstance().reference

        val userListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }

            override fun onDataChange(data: DataSnapshot) {
                currentUser = data.getValue(User::class.java)
                if (isSettingUp) {
                    setupGroupChatListener()
                    isSettingUp = false
                }
            }
        }

        database.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(userListener)
    }

    private fun setupGroupChatListener() {
        val groupsListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }

            override fun onDataChange(data: DataSnapshot) {
                groupChatList.clear()
                for (group in data.children) {
                    val conversation = group.getValue(Conversation::class.java) ?: continue
                    if (conversation.userIds.any { it == currentUser!!.id }) {
                        groupChatList.add(conversation)
                    }
                }

                view.bindGroupChatList(groupChatList)
            }
        }

        database.child("conversations").addValueEventListener(groupsListener)
    }

}