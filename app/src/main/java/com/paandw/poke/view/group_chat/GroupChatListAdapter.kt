package com.paandw.poke.view.group_chat

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.view.custom.GroupChatListItem

class GroupChatListAdapter (private val presenter: GroupChatListPresenter) : RecyclerView.Adapter<GroupChatListAdapter.ViewHolder>() {

    private val dataItems = ArrayList<Conversation>()

    fun setDataItems(items: MutableList<Conversation>) {
        dataItems.clear()
        dataItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GroupChatListItem(parent.context))
    }

    override fun getItemCount(): Int = dataItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friendItem.setup(dataItems[position], presenter)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var friendItem = itemView as GroupChatListItem
    }

}