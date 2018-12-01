package com.paandw.poke.view.friends

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.paandw.poke.data.models.Friend
import com.paandw.poke.view.custom.FriendListItem

class FriendListAdapter (private val presenter: FriendsPresenter) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {

    private val friendsList = ArrayList<Friend>()

    fun setDataItems(items: MutableList<Friend>) {
        friendsList.clear()
        friendsList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FriendListItem(parent.context))
    }

    override fun getItemCount(): Int = friendsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friendItem.setup(friendsList[position], presenter)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var friendItem = itemView as FriendListItem
    }

}