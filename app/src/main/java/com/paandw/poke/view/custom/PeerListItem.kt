package com.paandw.poke.view.custom

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.paandw.poke.R
import kotlinx.android.synthetic.main.view_peer_list_item.view.*

/**
 * TODO: document your custom view class.
 */
class PeerListItem(context: Context) : FrameLayout(context) {

    init {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.view_peer_list_item, this)
    }

    fun setup(device: WifiP2pDevice) {
        tv_name.text = device.deviceName
    }

}
