package com.paandw.poke.view.chat.p2p

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.paandw.poke.R
import com.paandw.poke.data.p2p.P2PBroadcastReceiver
import com.paandw.poke.view.chat.p2p.lobby.P2PDialog
import com.paandw.poke.view.chat.p2p.lobby.P2PLobbyPresenter
import kotlinx.android.synthetic.main.activity_p2p_lobby.*

class P2PLobbyActivity : AppCompatActivity(), IP2PLobbyView {

    private lateinit var presenter: P2PLobbyPresenter

    private val intentFilter = IntentFilter()
    private var peerDialog: P2PDialog? = null
    private var progressDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2p_lobby)

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        val manager = getSystemService(Context.WIFI_P2P_SERVICE)?.let { it as WifiP2pManager}
        val channel = manager?.initialize(this, mainLooper, null)
        presenter = P2PLobbyPresenter(this)

        if (manager != null) {
            presenter.start(manager, channel!!)
            peerDialog = P2PDialog()
            peerDialog?.setup(presenter)

            progressDialog = MaterialDialog.Builder(this)
                    .progress(true, 0)
                    .content("Searching for users...")
                    .build()

            btn_scan_for_users.setOnClickListener {
                presenter.scanForUsers()
            }
        }
    }

    override fun showPeerSelection(peers: MutableList<WifiP2pDevice>) {
        peerDialog?.setPeerList(peers)
        peerDialog?.show(supportFragmentManager, "peer_selection_dialog")
    }

    override fun beginConversation(device: WifiP2pDevice) {
        peerDialog?.dismiss()

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

    override fun showProgress() {
        progressDialog?.show()
    }

    override fun hideProgress() {
        progressDialog?.hide()
    }

    override fun showWifiP2PWarning() {
        MaterialDialog.Builder(this)
                .title("Error")
                .content("Please enable WiFi Direct in your device settings to allow for peer-to-peer communication")
                .positiveText("Okay")
                .show()
    }
}
