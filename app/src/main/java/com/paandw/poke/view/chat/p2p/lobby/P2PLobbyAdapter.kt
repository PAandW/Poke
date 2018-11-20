package com.paandw.poke.view.chat.p2p.lobby

import android.net.wifi.p2p.WifiP2pDevice
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.paandw.poke.view.custom.PeerListItem

internal class P2PLobbyAdapter (private val presenter: P2PLobbyPresenter) : RecyclerView.Adapter<P2PLobbyAdapter.ViewHolder>() {

    private val searchItems = ArrayList<WifiP2pDevice>()

    fun setListItems(items: MutableList<WifiP2pDevice>) {
        searchItems.clear()
        searchItems.addAll(items)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): P2PLobbyAdapter.ViewHolder {
        return ViewHolder(PeerListItem(parent.context))
    }

    override fun getItemCount(): Int = searchItems.size

    override fun onBindViewHolder(holder: P2PLobbyAdapter.ViewHolder, position: Int) {
        holder.searchItem.setup(searchItems[position], presenter)
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var searchItem: PeerListItem = itemView as PeerListItem

    }

}