package com.paandw.poke.view.messaging

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.paandw.poke.R
import kotlinx.android.synthetic.main.toolbar.*

class MessagingActivity : AppCompatActivity(), IMessagingView {

    private lateinit var presenter: MessagingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)
        presenter = MessagingPresenter()

        toolbar.title = intent?.extras?.getString("recipient_name")

        presenter.start(this, intent?.extras?.getString("chat_id")!!)
    }
}
