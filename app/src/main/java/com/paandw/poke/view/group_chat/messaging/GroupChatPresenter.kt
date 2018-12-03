package com.paandw.poke.view.group_chat.messaging

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.data.models.Message
import java.util.*

class GroupChatPresenter {

    private lateinit var view: IGroupChat
    private lateinit var currentUserId: String
    private lateinit var database: DatabaseReference
    private lateinit var currentUsername: String
    private lateinit var chatId: String
    private val messages = ArrayList<Message>()

    fun start(view: IGroupChat, chatId: String) {
        this.view = view
        this.chatId = chatId
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        currentUsername = FirebaseAuth.getInstance().currentUser!!.displayName!!
        database = FirebaseDatabase.getInstance().reference

        database.child("conversations").child(chatId).runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                //Do nothing
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                var conversation = data.getValue(Conversation::class.java)
                if (conversation == null) {
                    return Transaction.success(data)
                } else {
                    if (conversation.messages == null) {
                        conversation.messages = ArrayList<Message>()
                        val messageSlug = Message()
                        messageSlug.content = "private_message_init_slug"
                        conversation.messages.add(messageSlug)
                    }

                    data.value = conversation

                    return Transaction.success(data)
                }
            }
        })

        val messageListener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Do nothing
            }

            override fun onDataChange(data: DataSnapshot) {
                val conversation = data.getValue(Conversation::class.java)!!
                setupMessagesForDisplay(conversation.messages)

                view.bindMessages(messages, currentUserId)
            }
        }

        database.child("conversations").child(chatId).addValueEventListener(messageListener)
    }

    fun sendMessage(messageText: String) {
        val message = Message()
        message.content = messageText
        message.senderId = currentUserId
        message.senderName = currentUsername
        message.chatId = chatId
        message.sendTime = Date().toString()

        database.child("conversations").child(chatId).runTransaction(object: Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                view.bindMessages(messages, currentUserId)
                view.clearMessageText()
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                val conversation = data.getValue(Conversation::class.java)
                conversation!!.messages.add(message)
                setupMessagesForDisplay(conversation.messages)
                data.value = conversation
                return Transaction.success(data)
            }
        }, true)
    }

    private fun setupMessagesForDisplay(messagesFromDatabase: MutableList<Message>) {
        messages.clear()
        for (message in messagesFromDatabase) {
            if (messagesFromDatabase.indexOf(message) == 0) {
                continue
            }
            messages.add(message)
        }
    }

}