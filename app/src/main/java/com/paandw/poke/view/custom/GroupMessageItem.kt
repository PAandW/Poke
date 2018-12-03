package com.paandw.poke.view.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.paandw.poke.R
import com.paandw.poke.data.models.Message
import kotlinx.android.synthetic.main.view_group_conversation_item.view.*

/**
 * TODO: document your custom view class.
 */
class GroupMessageItem(context: Context) : FrameLayout(context) {

    init {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.view_group_conversation_item, this)
    }

    fun setup(message: Message, userId: String) {
        if (message.senderId == userId) {
            container_message_received.visibility = View.GONE
            container_message_sent.visibility = View.VISIBLE
            tv_received_name.visibility = View.GONE
            tv_sent_name.visibility = View.VISIBLE

            tv_message_sent.text = message.content
        } else {
            container_message_sent.visibility = View.GONE
            container_message_received.visibility = View.VISIBLE
            tv_received_name.visibility = View.VISIBLE
            tv_sent_name.visibility = View.GONE

            tv_received_name.text = message.senderName
            tv_message_received.text = message.content
        }
    }

}
