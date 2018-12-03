package com.paandw.poke.view.group_chat.create

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.data.models.Friend
import com.paandw.poke.view.custom.GroupCreationListItem

class GroupCreationAdapter (private val presenter: GroupCreationPresenter) : RecyclerView.Adapter<GroupCreationAdapter.ViewHolder>() {

    private val dataItems = ArrayList<Friend>()
    private val selectedDataItems = ArrayList<String>()

    fun setDataItems(items: MutableList<Friend>, selectedItems: MutableList<String>) {
        dataItems.clear()
        dataItems.addAll(items)
        selectedDataItems.clear()
        selectedDataItems.addAll(selectedItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GroupCreationListItem(parent.context))
    }

    override fun getItemCount(): Int = dataItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friendItem.setup(dataItems[position], presenter, selectedDataItems.any { it == dataItems[position].id })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var friendItem = itemView as GroupCreationListItem
    }

}