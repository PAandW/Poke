package com.paandw.poke.view.messaging

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.paandw.poke.R
import com.paandw.poke.data.models.Message
import kotlinx.android.synthetic.main.activity_messaging.*
import kotlinx.android.synthetic.main.fragment_p2p_dialog.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_friend_list_item.*

class MessagingActivity : AppCompatActivity(), IMessagingView {

    private lateinit var presenter: MessagingPresenter
    private lateinit var adapter: MessagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)
        presenter = MessagingPresenter()
        adapter = MessagingAdapter()

        toolbar.title = intent?.extras?.getString("recipient_name")

        rv_messages.layoutManager = LinearLayoutManager(this)
        rv_messages.adapter = adapter

        presenter.start(this, intent?.extras?.getString("chat_id")!!, toolbar.title.toString(), intent.extras?.getString("recipient_id")!!)

        btn_send.setOnClickListener {
            if (et_message.text.isNotEmpty()) {
                presenter.sendMessage(et_message.text.toString())
            }
        }

        rv_messages.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                rv_messages.post { rv_messages.scrollToPosition(adapter.itemCount - 1) }
            }
        }
    }

    override fun bindMessages(messages: MutableList<Message>, currentUserId: String) {
        adapter.setListItems(messages, currentUserId)
        rv_messages.scrollToPosition(messages.size - 1)
    }

    override fun clearMessageText() {
        et_message.text.clear()
    }
}
