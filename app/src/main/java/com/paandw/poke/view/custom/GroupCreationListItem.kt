package com.paandw.poke.view.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.paandw.poke.R
import com.paandw.poke.data.models.Friend
import com.paandw.poke.view.friends.FriendsPresenter
import com.paandw.poke.view.group_chat.create.GroupCreationPresenter
import kotlinx.android.synthetic.main.view_group_creation_item.view.*

class GroupCreationListItem(context: Context) : FrameLayout (context) {

    private var setup = true
    private var presenter: GroupCreationPresenter? = null
    private var friend: Friend? = null

    init {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.view_group_creation_item, this)

        setOnClickListener { cb_add_user.isChecked = !cb_add_user.isChecked }

        cb_add_user.setOnCheckedChangeListener { _, isChecked ->
            if (!setup) {
                presenter?.setFriendSelected(friend!!.id, isChecked)
            }
        }
    }

    fun setup(friend: Friend, presenter: GroupCreationPresenter, selected: Boolean) {
        setup = true
        this.friend = friend
        this.presenter = presenter
        tv_name.text = friend.username
        cb_add_user.isChecked = selected

        setup = false

    }

}