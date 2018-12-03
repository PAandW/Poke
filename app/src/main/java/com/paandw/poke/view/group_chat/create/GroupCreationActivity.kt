package com.paandw.poke.view.group_chat.create

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.paandw.poke.R

class GroupCreationActivity : AppCompatActivity(), IGroupCreation {

    private lateinit var presenter: GroupCreationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation)
        presenter = GroupCreationPresenter()
        presenter.start(this)
    }

    override fun bindFriendList() {

    }
}
