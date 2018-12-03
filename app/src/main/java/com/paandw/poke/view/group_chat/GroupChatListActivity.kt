package com.paandw.poke.view.group_chat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.paandw.poke.R
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.view.group_chat.create.GroupCreationActivity
import com.paandw.poke.view.group_chat.messaging.GroupChatActivity
import kotlinx.android.synthetic.main.activity_group_chat_list.*

class GroupChatListActivity : AppCompatActivity(), IGroupChatList {

    private lateinit var presenter: GroupChatListPresenter
    private lateinit var adapter: GroupChatListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_list)
        presenter = GroupChatListPresenter()
        adapter = GroupChatListAdapter(presenter)

        toolbar.title = "Group Chats"
        toolbar.inflateMenu(R.menu.menu_group_chat_list)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_create_group) {
                startActivity(Intent(this, GroupCreationActivity::class.java))
            }
            true
        }
        rv_group_chat_list.layoutManager = LinearLayoutManager(this)
        rv_group_chat_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_group_chat_list.adapter = adapter

        presenter.start(this)
    }

    override fun bindGroupChatList(conversations: MutableList<Conversation>) {
        adapter.setDataItems(conversations)
    }

    override fun toGroupChat(conversation: Conversation) {
        val intent = Intent(this, GroupChatActivity::class.java)
        intent.putExtra("chat_id", conversation.chatId)
        intent.putExtra("chat_name", conversation.groupName)
        startActivity(intent)
    }
}
