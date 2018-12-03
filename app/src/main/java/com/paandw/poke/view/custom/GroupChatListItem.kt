package com.paandw.poke.view.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.paandw.poke.R
import com.paandw.poke.data.models.Conversation
import com.paandw.poke.view.group_chat.GroupChatListPresenter
import kotlinx.android.synthetic.main.view_group_chat_list_item.view.*

class GroupChatListItem(context: Context) : FrameLayout(context) {

    init {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.view_friend_list_item, this)
    }

    fun setup(conversation: Conversation, presenter: GroupChatListPresenter) {
        tv_group_chat_name.text = conversation.groupName
    }

}