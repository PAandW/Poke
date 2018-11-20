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
        tv_message.text = p2pMessage.message
    }

}
