package com.devils.binance.net

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 * Created by AndyL on 2017/10/17.
 *
 */
abstract class AbstractMarketObserver : WebSocketListener() {

    abstract fun connect()

    abstract fun disconnect()

    abstract fun onReceiveMessage(webSocket: WebSocket?, text: String?)

    override fun onOpen(webSocket: WebSocket?, response: Response?) {
        webSocket?.send("")
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {

    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        webSocket?.close(1000, null)
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        onReceiveMessage(webSocket, text)
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {

    }

    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {

    }
}