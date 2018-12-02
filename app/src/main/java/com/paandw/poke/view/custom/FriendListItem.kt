package com.paandw.poke.view.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.paandw.poke.R
import com.paandw.poke.data.models.Friend
import com.paandw.poke.view.friends.FriendsPresenter
import kotlinx.android.synthetic.main.view_friend_list_item.view.*

class FriendListItem(context: Context) : FrameLayout (context) {

    init {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.view_friend_list_item, this)
    }

    fun setup(friend: Friend, presenter: FriendsPresenter) {
        if (!friend.sent && !friend.added) {
            container_pending_request.visibility = View.VISIBLE
            container_friend_options.visibility = View.GONE
            tv_pending.visibility = View.GONE
        } else if (friend.sent && !friend.added) {
            container_pending_request.visibility = View.INVISIBLE
            container_friend_options.visibility = View.GONE
            tv_pending.visibility = View.VISIBLE
        }
        else {
            container_friend_options.visibility = View.VISIBLE
            container_pending_request.visibility = View.INVISIBLE
            tv_pending.visibility = View.GONE
        }

        btn_add.setOnClickListener { presenter.addFriend(friend) }
        btn_remove_friend.setOnClickListener { presenter.removeFriend(friend) }
        btn_reject.setOnClickListener { presenter.rejectFriendRequest(friend) }
        btn_message.setOnClickListener { presenter.startPrivateMessaging(friend.privateMessageId, friend.username) }
        tv_name.text = friend.username
    }

}