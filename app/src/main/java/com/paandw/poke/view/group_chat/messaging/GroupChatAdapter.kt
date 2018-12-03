package com.paandw.poke.view.group_chat.messaging

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.paandw.poke.data.models.Message
import com.paandw.poke.view.custom.GroupMessageItem

class GroupChatAdapter (private val presenter: GroupChatPresenter) : RecyclerView.Adapter<GroupChatAdapter.ViewHolder>() {

    private val dataItems = ArrayList<Message>()
    private lateinit var userId: String

    fun setDataItems(items: MutableList<Message>, userId: String) {
        this.userId = userId
        dataItems.clear()
        dataItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GroupMessageItem(parent.context))
    }

    override fun getItemCount(): Int = dataItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friendItem.setup(dataItems[position], userId)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var friendItem = itemView as GroupMessageItem
    }

}