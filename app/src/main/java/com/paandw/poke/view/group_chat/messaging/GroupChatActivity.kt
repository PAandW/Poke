package com.paandw.poke.view.group_chat.messaging

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.paandw.poke.R
import com.paandw.poke.data.models.Message
import kotlinx.android.synthetic.main.activity_group_chat.*
import kotlinx.android.synthetic.main.toolbar.*

class GroupChatActivity : AppCompatActivity(), IGroupChat {

    private lateinit var presenter: GroupChatPresenter
    private lateinit var adapter: GroupChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        presenter = GroupChatPresenter()
        adapter = GroupChatAdapter(presenter)

        toolbar.title = intent?.extras?.getString("chat_name")

        rv_messages.layoutManager = LinearLayoutManager(this)
        rv_messages.adapter = adapter

        rv_messages.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                rv_messages.post { rv_messages.scrollToPosition(adapter.itemCount - 1) }
            }
        }

        btn_send.setOnClickListener {
            if (!et_message.text.isEmpty()) {
                presenter.sendMessage(et_message.text.toString())
            }
        }

        presenter.start(this, intent?.extras?.getString("chat_id")!!)
    }

    override fun bindMessages(messages: MutableList<Message>, userId: String) {
        adapter.setDataItems(messages, userId)
        rv_messages.scrollToPosition(messages.size - 1)
    }

    override fun clearMessageText() {
        et_message.text.clear()
    }
}
