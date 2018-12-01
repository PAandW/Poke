package com.paandw.poke.view.friends

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.paandw.poke.data.models.Friend
import com.paandw.poke.data.models.User

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

        if (friendToAddAsUser!!.pendingFriends == null) {
            friendToAddAsUser!!.pendingFriends = ArrayList<Friend>()
        }
        if (friendToAddAsUser!!.pendingFriends!!.find { it.id == currentUser!!.id } == null) {
            val currentUserAsFriend = Friend()
            currentUserAsFriend.id = currentUser!!.id
            currentUserAsFriend.username = currentUser!!.username
            currentUserAsFriend.sent = false
            friendToAddAsUser!!.pendingFriends!!.add(currentUserAsFriend)
        }

        database.child("users").child(currentUser!!.id).child("pendingFriends").setValue(currentUser!!.pendingFriends)
        database.child("users").child(friendToAddAsUser!!.id).child("pendingFriends").setValue(friendToAddAsUser!!.pendingFriends)

        setupFriendListForDisplay()
    }

    fun rejectFriendRequest(friend: Friend) {
        val rejectedUser = userList.find { it.id == friend.id } ?: return
        rejectedUser.pendingFriends.removeAll { it.id == currentUser!!.id }

        currentUser!!.pendingFriends.removeAll { it.id == friend.id }

        database.child("users").child(currentUser!!.id).child("pendingFriends").setValue(currentUser!!.pendingFriends)
        database.child("users").child(rejectedUser.id).child("pendingFriends").setValue(rejectedUser.pendingFriends)

        setupFriendListForDisplay()
    }

    fun addFriend(friend: Friend) {
        if (currentUser == null) {
            return
        }
        if (currentUser!!.friendsList == null) {
            currentUser!!.friendsList = ArrayList<Friend>()
        }
        if (currentUser!!.friendsList!!.find { it.id == friend.id } == null) {
            friend.added = true
            currentUser!!.pendingFriends.removeAll { it.id == friend.id }
            currentUser!!.friendsList!!.add(friend)
        }

        val friendAsUser = User()
        friendAsUser.id = friend.id
        friendAsUser.username = friend.username
        friendAsUser.friendsList = userList.find { it.id == friend.id }!!.friendsList
        friendAsUser.pendingFriends = userList.find { it.id == friend.id}!!.pendingFriends

        if (friendAsUser.friendsList == null) {
            friendAsUser.friendsList = ArrayList<Friend>()
        }
        if (friendAsUser.pendingFriends != null) {
            friendAsUser.pendingFriends.removeAll { it.id == currentUser!!.id }
        }
        if (friendAsUser.friendsList!!.find { it.id == currentUser!!.id } == null) {
            val currentUserAsFriend = Friend()
            currentUserAsFriend.id = currentUser!!.id
            currentUserAsFriend.username = currentUser!!.username
            currentUserAsFriend.added = true
            friendAsUser.friendsList!!.add(currentUserAsFriend)
        }

        database.child("users").child(currentUser!!.id).child("friendsList").setValue(currentUser!!.friendsList)
        database.child("users").child(friendAsUser.id).child("friendsList").setValue(friendAsUser.friendsList)
        database.child("users").child(currentUser!!.id).child("pendingFriends").setValue(currentUser!!.pendingFriends)
        database.child("users").child(friendAsUser.id).child("pendingFriends").setValue(friendAsUser.pendingFriends)

        setupFriendListForDisplay()
    }

}