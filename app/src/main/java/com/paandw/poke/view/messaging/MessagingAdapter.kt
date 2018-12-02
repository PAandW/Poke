package com.paandw.poke.view.messaging

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.paandw.poke.data.models.Message
import com.paandw.poke.view.custom.ConversationItem
import com.paandw.poke.view.custom.MessageItem

internal class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.ViewHolder>() {

    private val messages = ArrayList<Message>()
    private var currentUserId: String? = null

    fun setListItems(items: MutableList<Message>, currentUserId: String) {
        this.currentUserId = currentUserId
        messages.clear()
        messages.addAll(items)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MessageItem(parent.context))
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.searchItem.setup(messages[position], currentUserId!!)
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var searchItem: MessageItem = itemView as MessageItem

    }

}