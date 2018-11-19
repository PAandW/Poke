package com.paandw.poke.view.chat.p2p.lobby

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import com.paandw.poke.data.p2p.P2PBroadcastReceiver
import com.paandw.poke.view.chat.p2p.IP2PLobbyView

class P2PLobbyPresenter(var view: IP2PLobbyView) : WifiP2pManager.ChannelListener, WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    private lateinit var device: WifiP2pDevice
    private var manager: WifiP2pManager? = null
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var broadcastReceiver: P2PBroadcastReceiver

    private var peerList = ArrayList<WifiP2pDevice>()

    var isWifiP2pEnabled: Boolean = false

    fun start(manager: WifiP2pManager, channel: WifiP2pManager.Channel) {
        this.manager = manager
        this.channel = channel
    }

    fun onResume() {
        if (manager == null) {
            view.showWifiP2PWarning()
        } else {
            broadcastReceiver = P2PBroadcastReceiver(manager!!, channel, this)
            view.registerP2PReceiver(broadcastReceiver)
        }
    }

    fun onPause() {
        if (manager != null) {
            view.unregisterP2PReceiver(broadcastReceiver)
        }
    }

    fun userSelected(device: WifiP2pDevice) {

    }

    override fun onChannelDisconnected() {
        //TODO Handle this
    }

    override fun onPeersAvailable(peers: WifiP2pDeviceList) {
        view.hideProgress()
        peerList.clear()
        peerList.addAll(peers.deviceList)

    }

    override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
        //TODO Handle this
    }

    fun updateThisDevice(device: WifiP2pDevice) {
        this.device = device
    }

    fun scanForUsers() {
        view.showProgress()
        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                //Nothing really needs to go here, just means it didn't fail
            }

            override fun onFailure(reason: Int) {
                //TODO handle failed peer search initiation
            }
        })
    }
}