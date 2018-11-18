package com.paandw.pieceofcake.view.chat.p2p

import com.paandw.pieceofcake.data.p2p.P2PBroadcastReceiver

interface IP2PLobbyView {
    fun registerP2PReceiver(receiver: P2PBroadcastReceiver)
    fun unregisterP2PReceiver(receiver: P2PBroadcastReceiver)
}