package com.paandw.poke.view.chat.p2p

import android.net.wifi.p2p.WifiP2pInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.paandw.poke.R
import com.paandw.poke.data.p2p.P2PMessage
import kotlinx.android.synthetic.main.activity_p2p_chat.*

class P2PChatActivity : AppCompatActivity(), IP2PChatView {

    private lateinit var adapter: P2PMessageAdapter
    private lateinit var presenter: P2PChatPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2p_chat)

        presenter = P2PChatPresenter()
        adapter = P2PMessageAdapter()
        rv_messages.layoutManager = LinearLayoutManager(this)
        rv_messages.adapter = adapter

        presenter.start(intent?.extras?.get("info") as WifiP2pInfo, this)

        iv_send.setOnClickListener { presenter.sendMessage(et_message.text.toString()) }
    }

    override fun bindMessages(messages: MutableList<P2PMessage>) {
        adapter.setListItems(messages)
    }
}
