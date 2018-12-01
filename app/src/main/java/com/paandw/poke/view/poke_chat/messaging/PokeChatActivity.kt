package com.paandw.poke.view.poke_chat.messaging

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.paandw.poke.R
import com.paandw.poke.data.p2p.P2PMessage
import kotlinx.android.synthetic.main.activity_p2p_chat.*
import kotlinx.android.synthetic.main.toolbar.*

class PokeChatActivity : AppCompatActivity(), IPokeChatView {

    private lateinit var adapter: PokeChatAdapter
    private lateinit var presenter: PokeChatPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2p_chat)

        val connectedDeviceName = intent?.extras?.get("connected_device_name") as String

        toolbar.title = connectedDeviceName
        presenter = PokeChatPresenter()
        adapter = PokeChatAdapter()
        rv_messages.layoutManager = LinearLayoutManager(this)
        rv_messages.adapter = adapter

        presenter.start(intent?.extras?.get("info") as WifiP2pInfo, this)

        iv_send.setOnClickListener {
            val message = et_message.text.toString()
            et_message.text.clear()
            presenter.sendMessage(message)
        }
    }

    override fun bindMessages(messages: MutableList<P2PMessage>) {
        runOnUiThread {
            adapter.setListItems(messages)
            rv_messages.scrollToPosition(messages.count() - 1)
        }
    }
}
