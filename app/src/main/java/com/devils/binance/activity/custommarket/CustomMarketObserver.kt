package com.devils.binance.activity.custommarket

import com.devils.binance.App
import com.devils.binance.net.AbstractMarketObserver
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

/**
 * Created by AndyL on 2017/10/17.
 *
 */
class CustomMarketObserver(app : App, url : String, watcher : CustomMarketObserverWatcher) : AbstractMarketObserver() {

    var isObserving = false

    private val mApp     = app
    private val mUrl     = url
    private val mWatcher = watcher

    private var mOkHttpClient : OkHttpClient? = null
    private var mWebSocket : WebSocket? = null

    override fun connect() {
        val request = Request.Builder().url(mUrl)
                .addHeader("Connection", "keep-alive").build()
        mOkHttpClient = mApp.createHttpClient()
        mWebSocket = mOkHttpClient?.newWebSocket(request, this)
        mOkHttpClient?.dispatcher()?.executorService()?.shutdown()
        isObserving = true
    }

    override fun disconnect() {
        mOkHttpClient?.dispatcher()?.cancelAll()
        mWebSocket?.close(1000, "closed")
        mWebSocket?.cancel()
        isObserving = false
    }

    override fun onReceiveMessage(webSocket: WebSocket?, text: String?) {
        mWatcher.onReceiveMessage(webSocket, text)
    }

    interface CustomMarketObserverWatcher {
        fun onReceiveMessage(webSocket: WebSocket?, text: String?)
    }

}