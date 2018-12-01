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

        database.child("users").child(friendToAdd!!.id).runTransaction(object: Transaction.Handler {

            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
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
        if (currentUser!!.friendsList == null) {
            currentUser!!.friendsList = ArrayList<Friend>()
        }
        if (currentUser!!.friendsList!!.find { it.id == friend.id } == null) {
            friend.added = true
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

}