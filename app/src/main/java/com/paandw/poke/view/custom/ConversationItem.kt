package com.paandw.poke.view.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.paandw.poke.R
import com.paandw.poke.data.p2p.P2PMessage
import kotlinx.android.synthetic.main.view_conversation_item.view.*

/**
 * TODO: document your custom view class.
 */
class ConversationItem(context: Context) : FrameLayout(context) {

    init {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.view_conversation_item, this)
    }

    fun setup(p2pMessage: P2PMessage) {
        if (p2pMessage.isMine) {
            container_message_received.visibility = View.GONE
            container_message_sent.visibility = View.VISIBLE
            tv_message_sent.text = p2pMessage.message
        } else {
            container_message_sent.visibility = View.GONE
            container_message_received.visibility = View.VISIBLE
            tv_message_received.text = p2pMessage.message
        }
    }

}
