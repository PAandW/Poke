package com.paandw.poke.view.friends

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import com.paandw.poke.R
import com.paandw.poke.data.models.Friend
import com.paandw.poke.data.models.User
import kotlinx.android.synthetic.main.activity_friends.*

class FriendsActivity : AppCompatActivity(), IFriendsView {

    private lateinit var presenter: FriendsPresenter
    private lateinit var searchAdapter: ArrayAdapter<String>
    private lateinit var friendListAdapter: FriendListAdapter
    private var searchItems = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        presenter = FriendsPresenter()

        friendListAdapter = FriendListAdapter(presenter)
        rv_friends.layoutManager = LinearLayoutManager(this)
        rv_friends.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_friends.adapter = friendListAdapter

        searchAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, searchItems)
        et_add_friend.setAdapter(searchAdapter)

        et_add_friend.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {
                presenter.initiateSearch(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Don't need to do anything
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Don't need to do anything
            }
        })

        btn_add_friend.setOnClickListener { presenter.sendFriendRequest() }

        presenter.start(this)
    }

    override fun bindFriendList(friends: MutableList<Friend>?) {
        if (friends != null) {
            friendListAdapter.setDataItems(friends)
        }
    }

    override fun bindUserSearchItems(users: ArrayList<String>) {
        searchAdapter.clear()
        searchAdapter.addAll(users)
        searchAdapter.notifyDataSetChanged()
        et_add_friend.setOnItemClickListener { parent, view, position, id -> presenter.selectFriendFromSearch(position) }
    }
}
