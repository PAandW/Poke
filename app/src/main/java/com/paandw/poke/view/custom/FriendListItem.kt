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
        tv_name.text = friend.username
    }

}