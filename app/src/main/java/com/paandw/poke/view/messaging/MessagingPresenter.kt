package com.paandw.poke.view.messaging

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.data.models.Message
import java.util.*
import kotlin.collections.ArrayList

class MessagingPresenter {

    private lateinit var view: IMessagingView
    private lateinit var privateMessageId: String
    private lateinit var database: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private var messages = ArrayList<Message>()

    fun start(view: IMessagingView, privateMessageId: String, recipientName: String, recipientId: String) {
        this.view = view
        this.privateMessageId = privateMessageId
        database = FirebaseDatabase.getInstance().reference
        currentUser = FirebaseAuth.getInstance().currentUser!!

        database.child("conversations").child(this.privateMessageId).runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                //Do nothing
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                var conversation = data.getValue(Conversation::class.java)
                if (conversation != null) {
                    return Transaction.success(data)
                } else {
                    conversation = Conversation()
                    conversation.groupName = "private_message_placeholder"
                    conversation.userIds = ArrayList<String>()
                    conversation.userIds.add(currentUser.uid)
                    conversation.userIds.add(recipientId)
                    conversation.messages = ArrayList<Message>()
                    val messageSlug = Message()
                    messageSlug.content = "private_message_init_slug"
                    conversation.messages.add(messageSlug)

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

                view.bindMessages(messages, currentUser.uid)
            }
        }

        database.child("conversations").child(this.privateMessageId).addValueEventListener(messageListener)
    }

    fun sendMessage(messageText: String) {
        database.child("conversations").child(privateMessageId).runTransaction(object: Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                view.bindMessages(messages, currentUser.uid)
                view.clearMessageText()
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                val conversation = data.getValue(Conversation::class.java)
                val message = Message()
                message.chatId = privateMessageId
                message.senderId = currentUser.uid
                message.content = messageText
                message.sendTime = Date().toString()
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