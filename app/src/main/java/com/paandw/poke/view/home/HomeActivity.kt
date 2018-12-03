package com.paandw.poke.view.home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.paandw.poke.R
import com.paandw.poke.view.chat.p2p.PokeChatLobbyActivity
import com.paandw.poke.view.friends.FriendsActivity
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
            presenter.pokeChatClicked()
        }

        btn_friends.setOnClickListener {
            presenter.friendsListClicked()
        }

        btn_group_chats.setOnClickListener {
            //Todo to group chat list
        }

        btn_logout.setOnClickListener {
            presenter.logout()
        }
    }

    override fun toPokeChat() {
       startActivity(Intent(this, PokeChatLobbyActivity::class.java))
    }

    override fun toFriendsList() {
        startActivity(Intent(this, FriendsActivity::class.java))
    }

    override fun toGroupChats() {
        //TODO group chats
    }

    override fun toLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
