package com.paandw.pieceofcake.view.chat.p2p

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.paandw.pieceofcake.R
import com.paandw.pieceofcake.data.p2p.P2PBroadcastReceiver
import com.paandw.pieceofcake.view.chat.p2p.lobby.P2PLobbyPresenter
import kotlinx.android.synthetic.main.activity_p2p_lobby.*

class P2PLobbyActivity : AppCompatActivity(), IP2PLobbyView {

    private lateinit var presenter: P2PLobbyPresenter

    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var manager: WifiP2pManager
    private val intentFilter = IntentFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2p_lobby)

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)


        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        presenter.start(manager, channel, this)

        btn_scan_for_users.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun registerP2PReceiver(receiver: P2PBroadcastReceiver) {
        registerReceiver(receiver, intentFilter)
    }

    override fun unregisterP2PReceiver(receiver: P2PBroadcastReceiver) {
        unregisterReceiver(receiver)
    }
}
