package com.paandw.poke.view.home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.paandw.poke.R
import com.paandw.poke.view.login.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), IHomeView {

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        presenter = HomePresenter()
        presenter.start(this)

        toolbar.title = getString(R.string.app_name)

        btn_poke_chat.setOnClickListener {
            //Todo to poke chat
        }

        btn_friends.setOnClickListener {
            //Todo to friends list
        }

        btn_group_chats.setOnClickListener {
            //Todo to group chat list
        }

        btn_logout.setOnClickListener {
            presenter.logout()
        }
    }

    override fun toPokeChat() {
        //TODO poke chat
    }

    override fun toFriendsList() {
        //TODO friends list
    }

    override fun toGroupChat() {
        //TODO group chats
    }

    override fun toLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
