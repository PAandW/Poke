package com.paandw.poke.view.group_chat.create

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.paandw.poke.R
import com.paandw.poke.data.models.Friend
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

        }

        presenter.start(this)
    }

    override fun bindFriendList(friendList: MutableList<Friend>, addedFriends: MutableList<String>) {
        adapter.setDataItems(friendList, addedFriends)
        rv_friends.post { adapter.notifyDataSetChanged() }
    }

    override fun toGroupChat(chatId: String, chatName: String) {

    }
}
