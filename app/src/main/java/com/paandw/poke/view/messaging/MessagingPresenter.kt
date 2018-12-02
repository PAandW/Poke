package com.paandw.poke.view.messaging

class MessagingPresenter {

    private lateinit var view: IMessagingView
    private lateinit var privateMessageId: String

    fun start(view: IMessagingView, privateMessageId: String) {
        this.view = view
        this.privateMessageId = privateMessageId
    }

}