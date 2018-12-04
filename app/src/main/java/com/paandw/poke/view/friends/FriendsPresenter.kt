package com.paandw.poke.view.friends

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.data.models.Friend
import com.paandw.poke.data.models.Message
import com.paandw.poke.data.models.User
import java.util.*
import kotlin.collections.ArrayList

class FriendsPresenter {

    private lateinit var view: IFriendsView
    private lateinit var database: DatabaseReference
    private var currentUser: User? = null
    private var userList = ArrayList<User>()
    private var searchMatchList = ArrayList<User>()
    private var tempMatchList = ArrayList<User>()
    private var setup = true
    private var friendToAdd: Friend? = null
    private var friendToAddAsUser: User? = null

    fun start(view: IFriendsView) {
        this.view = view
        database = FirebaseDatabase.getInstance().reference
        val searchListener = object : ValueEventListener {
            override fun onCancelled(data: DatabaseError) {
                //Do nothing
            }

            override fun onDataChange(data: DataSnapshot) {
                userList.clear()
                for (id in data.children) {
                    if (id.key.toString() == (FirebaseAuth.getInstance().currentUser?.uid)) {
                        currentUser = id.getValue(User::class.java)!!
                        setupFriendListForDisplay()
                        continue
                    }
                    if (currentUser?.friendsList?.find { it.id == id.key } != null || currentUser?.pendingFriends?.find { it.id == id.key } != null) {
                        continue
                    }
                    userList.add(id.getValue(User::class.java)!!)
                }
            }
        }

        database.child("users").addValueEventListener(searchListener)
    }

    fun setupFriendListForDisplay() {
        if (currentUser == null) {
            return
        }
        val listForDisplay = ArrayList<Friend>()
        if (currentUser!!.pendingFriends != null) {
            listForDisplay.addAll(currentUser!!.pendingFriends)
        }
        if (currentUser!!.friendsList != null) {
            listForDisplay.addAll(currentUser!!.friendsList)
        }

        view.bindFriendList(listForDisplay)
    }

    fun initiateSearch(searchTerm: String) {
        tempMatchList.addAll(searchMatchList)
        searchMatchList.clear()
        friendToAdd = null
        val users = ArrayList<String>()
        for (user in userList) {
            if (user.username.contains(searchTerm)) {
                if (currentUser!!.friendsList != null) {
                    if (currentUser!!.friendsList.any { it.id == user.id }) {
                        continue
                    }
                }
                if (currentUser!!.pendingFriends != null) {
                    if (currentUser!!.pendingFriends.any { it.id == user.id }) {
                        continue
                    }
                }
                searchMatchList.add(user)
                users.add(user.username)
            }
        }

        view.bindUserSearchItems(users)
    }

    fun selectFriendFromSearch(position: Int) {
        friendToAdd = Friend()
        friendToAdd!!.id = tempMatchList[position].id
        friendToAdd!!.username = tempMatchList[position].username
        friendToAdd!!.sent = true
        friendToAddAsUser = User()
        friendToAddAsUser!!.id = tempMatchList[position].id
        friendToAddAsUser!!.username = tempMatchList[position].username
        friendToAddAsUser!!.friendsList = tempMatchList[position].friendsList
    }

    fun pokeAStranger() {
        val unaddedUsers = ArrayList<User>()

        for (user in userList) {
            if (currentUser!!.friendsList != null) {
                if (currentUser!!.friendsList.any { it.id == user.id }) {
                    continue
                }
            }
            if (currentUser!!.pendingFriends != null) {
                if (currentUser!!.pendingFriends.any { it.id == user.id }) {
                    continue
                }
            }

            unaddedUsers.add(user)
        }

        if (unaddedUsers.size == 0) {
            return
        }

        val randomIndex = Random().nextInt(unaddedUsers.size)

        friendToAdd = Friend()
        friendToAdd!!.id = unaddedUsers[randomIndex].id
        friendToAdd!!.username = unaddedUsers[randomIndex].username
        friendToAdd!!.sent = true
        friendToAddAsUser = User()
        friendToAddAsUser!!.id = unaddedUsers[randomIndex].id
        friendToAddAsUser!!.username = unaddedUsers[randomIndex].username
        friendToAddAsUser!!.friendsList = unaddedUsers[randomIndex].friendsList

        sendFriendRequest()
    }

    fun pokeFriend(friend: Friend) {

        val chatId = currentUser!!.friendsList.first { it.id == friend.id }.privateMessageId

        database.child("conversations").child(chatId).runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                sendPokeMessage(friend, chatId)
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                var conversation = data.getValue(Conversation::class.java)
                if (conversation != null) {
                    return Transaction.success(data)
                } else {
                    conversation = Conversation()
                    conversation.groupName = "private_message_placeholder"
                    conversation.userIds = ArrayList<String>()
                    conversation.userIds.add(currentUser!!.id)
                    conversation.userIds.add(friend.id)
                    conversation.messages = ArrayList<Message>()
                    val messageSlug = Message()
                    messageSlug.content = "private_message_init_slug"
                    conversation.messages.add(messageSlug)

                    data.value = conversation

                    return Transaction.success(data)
                }
            }
        })
    }

    fun sendFriendRequest() {
        if (friendToAdd == null || currentUser == null) {
            return
        }
        if (currentUser!!.pendingFriends == null) {
            currentUser!!.pendingFriends = ArrayList<Friend>()
        }
        if (currentUser!!.pendingFriends!!.find { it.id == friendToAdd!!.id } == null) {
            currentUser!!.pendingFriends!!.add(friendToAdd)
        }

        database.child("users").child(friendToAdd!!.id).runTransaction(object: Transaction.Handler {

            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                friendToAdd = null
                friendToAddAsUser = null
                setupFriendListForDisplay()
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                val friendAsUser = data.getValue(User::class.java)
                val currentUserAsFriend = Friend()
                currentUserAsFriend.id = currentUser!!.id
                currentUserAsFriend.username = currentUser!!.username
                currentUserAsFriend.sent = false
                currentUserAsFriend.added = false
                if (friendAsUser!!.pendingFriends == null) {
                    friendAsUser.pendingFriends = ArrayList<Friend>()
                }
                friendAsUser.pendingFriends.add(currentUserAsFriend)
                data.value = friendAsUser
                return Transaction.success(data)
            }
        }, true)

        database.child("users").child(currentUser!!.id).child("pendingFriends").setValue(currentUser!!.pendingFriends)

        setupFriendListForDisplay()
    }

    fun rejectFriendRequest(friend: Friend) {
        database.child("users").child(friend.id).runTransaction(object: Transaction.Handler {

            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                setupFriendListForDisplay()
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                val friendAsUser = data.getValue(User::class.java)
                friendAsUser!!.pendingFriends.removeAll { it.id == currentUser!!.id }
                data.value = friendAsUser
                return Transaction.success(data)
            }
        }, true)

        if (currentUser!!.pendingFriends!!.find { it.id == friend.id } != null) {
            currentUser!!.pendingFriends.removeAll { it.id == friend.id }
        }
        currentUser!!.pendingFriends.removeAll { it.id == friend.id }

        database.child("users").child(currentUser!!.id).child("pendingFriends").setValue(currentUser!!.pendingFriends)

        setupFriendListForDisplay()
    }

    fun removeFriend(friend: Friend) {
        if (currentUser == null) {
            return
        }
        if (currentUser!!.friendsList!!.find { it.id == friend.id } != null) {
            currentUser!!.friendsList!!.removeAll { it.id == friend.id }
        }

        database.child("users").child(friend.id).runTransaction(object: Transaction.Handler {

            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                setupFriendListForDisplay()
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                val friendAsUser = data.getValue(User::class.java)
                friendAsUser!!.friendsList.removeAll { it.id == currentUser!!.id }
                data.value = friendAsUser
                return Transaction.success(data)
            }
        }, true)

        database.child("users").child(currentUser!!.id).child("friendsList").setValue(currentUser!!.friendsList)
        setupFriendListForDisplay()
    }

    fun addFriend(friend: Friend) {
        if (currentUser == null) {
            return
        }
        val privateMessageId = UUID.randomUUID().toString()
        if (currentUser!!.friendsList == null) {
            currentUser!!.friendsList = ArrayList<Friend>()
        }
        if (currentUser!!.friendsList!!.find { it.id == friend.id } == null) {
            friend.added = true
            friend.privateMessageId = privateMessageId
            currentUser!!.pendingFriends.removeAll { it.id == friend.id }
            currentUser!!.friendsList!!.add(friend)
        }

        database.child("users").child(friend.id).runTransaction(object: Transaction.Handler {

            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                setupFriendListForDisplay()
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                val friendAsUser = data.getValue(User::class.java)
                val currentUserAsFriend = Friend()
                friendAsUser!!.pendingFriends.removeAll { it.id == currentUser!!.id }
                currentUserAsFriend.id = currentUser!!.id
                currentUserAsFriend.username = currentUser!!.username
                currentUserAsFriend.added = true
                currentUserAsFriend.privateMessageId = privateMessageId
                if (friendAsUser.friendsList == null) {
                    friendAsUser.friendsList = ArrayList<Friend>()
                }
                friendAsUser.friendsList.add(currentUserAsFriend)
                data.value = friendAsUser
                return Transaction.success(data)
            }
        }, true)

        database.child("users").child(currentUser!!.id).child("friendsList").setValue(currentUser!!.friendsList)
        database.child("users").child(currentUser!!.id).child("pendingFriends").setValue(currentUser!!.pendingFriends)

        setupFriendListForDisplay()
    }

    fun startPrivateMessaging(privateMessageId: String, friend: Friend) {
        view.toPrivateMessaging(privateMessageId, friend)
    }

    private fun sendPokeMessage(friend: Friend, chatId: String) {
        database.child("conversations").child(chatId).runTransaction(object: Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                view.showPokeConfirmation("You poked ${friend.username}!")
            }

            override fun doTransaction(data: MutableData): Transaction.Result {
                val conversation = data.getValue(Conversation::class.java)
                val message = Message()
                message.chatId = chatId
                message.senderId = currentUser!!.id
                message.content = "poke"
                message.sendTime = Date().toString()
                conversation!!.messages.add(message)
                data.value = conversation
                return Transaction.success(data)
            }
        }, true)
    }

}