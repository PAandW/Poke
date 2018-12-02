package com.paandw.poke.view.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.paandw.poke.R
import com.paandw.poke.data.models.Message
import com.paandw.poke.data.p2p.P2PMessage
import kotlinx.android.synthetic.main.view_conversation_item.view.*

/**
 * TODO: document your custom view class.
 */
class MessageItem(context: Context) : FrameLayout(context) {

    init {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.view_conversation_item, this)
    }

    fun setup(message: Message, userId: String) {
        if (message.senderId == userId) {
            container_message_received.visibility = View.GONE
            container_message_sent.visibility = View.VISIBLE
            tv_message_sent.text = message.content
        } else {
            container_message_sent.visibility = View.GONE
            container_message_received.visibility = View.VISIBLE
            tv_message_received.text = message.content
        }
    }

}
