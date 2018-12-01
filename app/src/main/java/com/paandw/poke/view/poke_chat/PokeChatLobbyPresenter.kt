package com.paandw.poke.view.chat.p2p.lobby

import android.net.wifi.p2p.*
import com.paandw.poke.data.p2p.P2PBroadcastReceiver
import com.paandw.poke.data.p2p.P2PMessage
import com.paandw.poke.view.chat.p2p.IPokeChatLobbyView

class PokeChatLobbyPresenter(var view: IPokeChatLobbyView) : WifiP2pManager.ChannelListener, WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    private lateinit var device: WifiP2pDevice
    private var manager: WifiP2pManager? = null
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var broadcastReceiver: P2PBroadcastReceiver
    private var messages = ArrayList<P2PMessage>()
    private var selectedUser: WifiP2pDevice? = null

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
        val config = WifiP2pConfig()
        selectedUser = device
        config.deviceAddress = device.deviceAddress
        manager?.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
            }

            override fun onFailure(p0: Int) {
            }
        })
    }

    override fun onChannelDisconnected() {
        //TODO Handle this
    }

    override fun onPeersAvailable(peers: WifiP2pDeviceList) {
        view.hideProgress()
        peerList.clear()
        peerList.addAll(peers.deviceList)
        view.bindPeerList(peerList)
    }

    override fun onConnectionInfoAvailable(info: WifiP2pInfo) {
        if (info.groupFormed) {
            if (selectedUser != null) {
                view.toChatActivity(info, selectedUser!!.deviceName)
            } else {
                view.toChatActivity(info, info.groupOwnerAddress.hostName)
            }
        }
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

    fun cancelUserSearch() {
        manager?.stopPeerDiscovery(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                //Do nothing really...
            }

            override fun onFailure(p0: Int) {
                //Also don't really need to do anything...
            }
        })
    }

    fun destroyExistingGroup() {
        manager?.removeGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                //Don't do anything
            }

            override fun onFailure(p0: Int) {
                //Also don't do anything
            }
        })
    }
}