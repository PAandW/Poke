package com.paandw.poke.view.home

import com.google.firebase.auth.FirebaseAuth

class HomePresenter {
    private lateinit var view: IHomeView

    fun start(view: IHomeView) {
        this.view = view
    }

    fun pokeChatClicked() {
        view.toPokeChat()
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        view.toLogin()
    }
}