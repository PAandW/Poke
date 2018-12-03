package com.paandw.poke.view.group_chat.create

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.paandw.poke.R
import com.paandw.poke.data.models.Friend
import com.paandw.poke.view.group_chat.messaging.GroupChatActivity
import kotlinx.android.synthetic.main.activity_group_creation.*

class GroupCreationActivity : AppCompatActivity(), IGroupCreation {

    private lateinit var presenter: GroupCreationPresenter
    private lateinit var adapter: GroupCreationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation)
        presenter = GroupCreationPresenter()
        adapter = GroupCreationAdapter(presenter)

        rv_friends.layoutManager = LinearLayoutManager(this)
        rv_friends.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_friends.adapter = adapter

        btn_create_group.setOnClickListener {
            if (!et_group_name.text.toString().isEmpty()) {
                presenter.createGroup(et_group_name.text.toString())
            }
        }

        presenter.start(this)
    }

    override fun bindFriendList(friendList: MutableList<Friend>, addedFriends: MutableList<String>) {
        adapter.setDataItems(friendList, addedFriends)
        rv_friends.post { adapter.notifyDataSetChanged() }
    }

    override fun toGroupChat(chatId: String, chatName: String) {
        val intent = Intent(this, GroupChatActivity::class.java)
        intent.putExtra("chat_id", chatId)
        intent.putExtra("chat_name", chatName)
        startActivity(intent)
        finish()
    }
}
