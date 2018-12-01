package com.paandw.poke.view.home

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.paandw.poke.data.models.User

class HomePresenter {

    private lateinit var view: IHomeView
    private lateinit var database: DatabaseReference

    fun start(view: IHomeView) {
        this.view = view
        database = FirebaseDatabase.getInstance().reference
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val user = User()
        user.id = firebaseUser?.uid
        user.username = firebaseUser?.displayName
        database.child("users").child(user.id).setValue(user)
    }

    fun pokeChatClicked() {
        view.toPokeChat()
    }

    fun friendsListClicked(){
        view.toFriendsList()
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        view.toLogin()
    }
}