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
                        if (setup) {
                            view.bindFriendList(currentUser!!.friendsList)
                            setup = false
                        }
                        continue
                    }
                    if (currentUser?.friendsList?.find { it.id == id.key } != null) {
                        continue
                    }
                    userList.add(id.getValue(User::class.java)!!)
                }
            }
        }

        database.child("users").addValueEventListener(searchListener)
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
        friendToAddAsUser = User()
        friendToAddAsUser!!.id = tempMatchList[position].id
        friendToAddAsUser!!.username = tempMatchList[position].username
        friendToAddAsUser!!.friendsList = tempMatchList[position].friendsList
    }

    fun addFriend() {
        if (friendToAdd == null || currentUser == null) {
            return
        }
        if (currentUser!!.friendsList == null) {
            currentUser!!.friendsList = ArrayList<Friend>()
        }
        if (currentUser!!.friendsList!!.find { it.id == friendToAdd!!.id } == null) {
            currentUser!!.friendsList!!.add(friendToAdd)
        }

        if (friendToAddAsUser!!.friendsList == null) {
            friendToAddAsUser!!.friendsList = ArrayList<Friend>()
        }
        if (friendToAddAsUser!!.friendsList!!.find { it.id == currentUser!!.id } == null) {
            val currentUserAsFriend = Friend()
            currentUserAsFriend.id = currentUser!!.id
            currentUserAsFriend.username = currentUser!!.username
            friendToAddAsUser!!.friendsList!!.add(currentUserAsFriend)
        }

        database.child("users").child(currentUser!!.id).child("friendsList").setValue(currentUser!!.friendsList)
        database.child("users").child(friendToAddAsUser!!.id).child("friendsList").setValue(friendToAddAsUser!!.friendsList)

        view.bindFriendList(currentUser!!.friendsList)
    }

}