package com.paandw.poke.view.chat.p2p.lobby

import android.app.Dialog
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.paandw.poke.R
import kotlinx.android.synthetic.main.fragment_p2p_dialog.*


class P2PDialog : DialogFragment() {

    private lateinit var adapter: P2PLobbyAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog.setTitle("Select User")
        dialog.setContentView(R.layout.fragment_p2p_dialog)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recycler_view.adapter = adapter
        return dialog
    }

    fun setup(presenter: P2PLobbyPresenter) {
        adapter = P2PLobbyAdapter(presenter)
    }

    fun setPeerList(peers: MutableList<WifiP2pDevice>) {
        adapter.setListItems(peers)
    }
}