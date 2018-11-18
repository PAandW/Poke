package com.paandw.pieceofcake.view.chat.p2p.lobby

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import com.paandw.pieceofcake.data.p2p.P2PBroadcastReceiver
import com.paandw.pieceofcake.view.chat.p2p.IP2PLobbyView

class P2PLobbyPresenter : WifiP2pManager.ChannelListener, WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    private lateinit var device: WifiP2pDevice
    private lateinit var manager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var broadcastReceiver: P2PBroadcastReceiver
    private lateinit var view: IP2PLobbyView

    private var peerList = ArrayList<WifiP2pDevice>()

    var isWifiP2pEnabled: Boolean = false

    fun start(manager: WifiP2pManager, channel: WifiP2pManager.Channel, view: IP2PLobbyView) {
        this.manager = manager
        this.channel = channel
        this.view = view
    }

    fun onResume() {
        broadcastReceiver = P2PBroadcastReceiver(manager, channel, this)
        view.registerP2PReceiver(broadcastReceiver)
    }

    fun onPause() {
        view.unregisterP2PReceiver(broadcastReceiver)
    }

    override fun onChannelDisconnected() {
        //TODO Handle this
    }

    override fun onPeersAvailable(peers: WifiP2pDeviceList) {
        peerList.clear()
        peerList.addAll(peers.deviceList)
    }

    override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
        //TODO Handle this
    }

    fun updateThisDevice(device: WifiP2pDevice) {
        this.device = device
    }

    fun initiatePeerSearch() {
        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                //Nothing really needs to go here, just means it didn't fail
            }

            override fun onFailure(reason: Int) {
                //TODO handle failed peer search initiation
            }
        })
    }
}