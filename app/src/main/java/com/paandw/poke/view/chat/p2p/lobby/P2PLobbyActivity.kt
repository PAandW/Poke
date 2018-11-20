package com.paandw.poke.view.chat.p2p

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.paandw.poke.R
import com.paandw.poke.data.p2p.P2PBroadcastReceiver
import com.paandw.poke.data.p2p.P2PMessage
import com.paandw.poke.data.service.P2PClient
import com.paandw.poke.data.service.P2PServer
import com.paandw.poke.view.chat.p2p.lobby.P2PDialog
import com.paandw.poke.view.chat.p2p.lobby.P2PLobbyPresenter
import com.paandw.poke.view.chat.p2p.lobby.P2PMessageAdapter
import kotlinx.android.synthetic.main.activity_p2p_lobby.*

class P2PLobbyActivity : AppCompatActivity(), IP2PLobbyView {

    private var presenter: P2PLobbyPresenter? = null

    private val intentFilter = IntentFilter()
    private var peerDialog: P2PDialog? = null
    private var progressDialog: MaterialDialog? = null
    private lateinit var messageAdapter: P2PMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2p_lobby)
        group_conversation.visibility = View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            initialize()
        }

    }

    private fun initialize() {

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        val manager = getSystemService(Context.WIFI_P2P_SERVICE)?.let { it as WifiP2pManager }
        val channel = manager?.initialize(this, mainLooper, null)
        presenter = P2PLobbyPresenter(this)

        messageAdapter = P2PMessageAdapter(presenter!!)
        rv_conversation.layoutManager = LinearLayoutManager(this)
        rv_conversation.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_conversation.adapter = messageAdapter

        if (manager != null) {
            presenter!!.start(manager, channel!!)
            peerDialog = P2PDialog()
            peerDialog?.setup(presenter!!)

            progressDialog = MaterialDialog.Builder(this)
                    .progress(true, 0)
                    .content("Searching for users...")
                    .build()

            btn_scan_for_users.setOnClickListener {
                presenter?.scanForUsers()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialize()
                presenter?.onResume()
            }
        }
    }

    override fun showPeerSelection(peers: MutableList<WifiP2pDevice>) {
        if (!peerDialog!!.isAdded && peers.size > 0) {
            peerDialog?.showNow(supportFragmentManager, "dialog")
        }
        peerDialog?.setPeerList(peers)
    }

    override fun userSelected(device: WifiP2pDevice) {
        peerDialog?.dismiss()
        //group_scan_for_users.visibility = View.GONE
        //group_conversation.visibility = View.VISIBLE
        presenter?.startConversation(device)
    }

    override fun bindMessages(messages: MutableList<P2PMessage>) {
        messageAdapter.setListItems(messages)
        btn_scan_for_users.text = messages[0].message
    }

    override fun initiateServer() {
        if (peerDialog!!.isVisible) {
            peerDialog?.dismiss()
        }
        //group_conversation.visibility = View.VISIBLE
        //group_scan_for_users.visibility = View.GONE
        P2PServer(presenter!!).execute()
    }

    override fun initiateClient(info: WifiP2pInfo) {
        if (peerDialog!!.isVisible) {
            peerDialog?.dismiss()
        }
        //group_conversation.visibility = View.VISIBLE
        //group_scan_for_users.visibility = View.GONE
        P2PClient(this, info).execute("Testing, testing, 1, 2, 3...")
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
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
        progressDialog?.dismiss()
    }

    override fun showWifiP2PWarning() {
        MaterialDialog.Builder(this)
                .title("Error")
                .content("Please enable WiFi Direct in your device settings to allow for peer-to-peer communication")
                .positiveText("Okay")
                .show()
    }
}
