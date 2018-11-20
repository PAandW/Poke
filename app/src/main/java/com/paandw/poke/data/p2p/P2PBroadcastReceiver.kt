package com.paandw.poke.data.p2p

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import com.paandw.poke.view.chat.p2p.lobby.P2PLobbyPresenter

class P2PBroadcastReceiver : BroadcastReceiver {

    private var manager: WifiP2pManager
    private var channel: WifiP2pManager.Channel
    private var listener: P2PLobbyPresenter

    constructor(manager: WifiP2pManager, channel: WifiP2pManager.Channel, listener: P2PLobbyPresenter) {
        this.manager = manager
        this.channel = channel
        this.listener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Determine if Wifi P2P mode is enabled or not, alert
                // the Activity.
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                listener.isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                manager.requestPeers(channel, listener)
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                manager.requestConnectionInfo(channel, listener)
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                (listener)
                        .apply {
                            updateThisDevice(
                                    intent.getParcelableExtra(
                                            WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice
                            )
                        }
            }
        }
    }

}