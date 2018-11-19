package com.paandw.poke.view.chat.p2p

import android.net.wifi.p2p.WifiP2pDevice
import com.paandw.poke.data.p2p.P2PBroadcastReceiver

interface IP2PLobbyView {
    fun registerP2PReceiver(receiver: P2PBroadcastReceiver)
    fun unregisterP2PReceiver(receiver: P2PBroadcastReceiver)
    fun showProgress()
    fun hideProgress()
    fun showWifiP2PWarning()
    fun showPeerSelection(peers: MutableList<WifiP2pDevice>)
    fun beginConversation(device: WifiP2pDevice)
}