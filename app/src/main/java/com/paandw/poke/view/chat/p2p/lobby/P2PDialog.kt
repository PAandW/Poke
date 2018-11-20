package com.paandw.poke.view.chat.p2p.lobby

import android.app.AlertDialog
import android.app.Dialog
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.paandw.poke.R
import kotlinx.android.synthetic.main.fragment_p2p_dialog.view.*


class P2PDialog : DialogFragment() {

    private lateinit var adapter: P2PLobbyAdapter
    private var presenter: P2PLobbyPresenter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select User")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_p2p_dialog, null)
        view.recycler_view.layoutManager = LinearLayoutManager(context)
        view.recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        view.recycler_view.adapter = adapter
        view.btn_close.setOnClickListener {
            presenter?.cancelUserSearch()
            dismiss()
        }
        builder.setView(view)
        return builder.create()
    }

    fun setup(presenter: P2PLobbyPresenter) {
        this.presenter = presenter
        adapter = P2PLobbyAdapter(this.presenter!!)
    }

    fun setPeerList(peers: MutableList<WifiP2pDevice>) {
        adapter.setListItems(peers)
    }
}