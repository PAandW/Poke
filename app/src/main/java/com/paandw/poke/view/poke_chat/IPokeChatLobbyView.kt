package com.paandw.poke.view.chat.p2p

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import com.paandw.poke.data.p2p.P2PBroadcastReceiver

interface IPokeChatLobbyView {
    fun registerP2PReceiver(receiver: P2PBroadcastReceiver)
    fun unregisterP2PReceiver(receiver: P2PBroadcastReceiver)
    fun showProgress()
    fun hideProgress()
    fun showWifiP2PWarning()
    fun bindPeerList(peers: MutableList<WifiP2pDevice>)
    fun toChatActivity(info: WifiP2pInfo, connectedDeviceName: String)
}