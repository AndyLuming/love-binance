package com.devils.binance.activity.main

import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.util.Log
import android.view.View
import android.widget.Toast
import com.devils.binance.App
import com.devils.binance.R
import com.devils.binance.base.BaseActivity
import com.devils.binance.bean.Trade
import com.devils.binance.data.ProductRespository
import com.devils.binance.net.NetCallback
import com.devils.binance.net.model.ProductsList
import com.devils.binance.widgets.ProgressView
import com.ysst.consultant.base.fragment.BaseFragment
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString




class MarketFragment : BaseFragment() {

    @JvmField val API_KEY = "tnjMgffaGXNbDMMzAJM1lcda2WVeU6VALHUSIqZrPOfJw9jlrCd1pvQbyreJlXJV"
    @JvmField val SEC_KEY = "TWoH08dALLgl5CH1vNk0XGuehtUmgs8fyXrHWtGuW9kxvw0L7ZLegUuB9QxKEb6j"

    override val layoutResId: Int
        get() = R.layout.fragment_market

    var marketName = ""

    private lateinit var refreshLayout : PtrFrameLayout
    private lateinit var recyclerView  : RecyclerView
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter       : MarketAdapter

    private val progress : ProgressView by lazy { ProgressView(mContext) }
    private val repo = ProductRespository()

    override fun setUpViews(root: View, savedInstanceState: Bundle?) {
        with(root){
            refreshLayout = findViewById(R.id.refreshLayout)
            recyclerView  = findViewById(R.id.recyclerView)
            layoutManager = LinearLayoutManager(mContext)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = layoutManager
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapter = MarketAdapter(mContext, marketName)
            recyclerView.adapter = adapter
        }
    }

    override fun preWork(savedInstanceState: Bundle?) {

    }

    override fun work(savedInstanceState: Bundle?) {
        progress.show()
        repo.fetchData(object : NetCallback<ProductsList>{

            override fun onSuccess(result: ProductsList?) {
                progress.dismiss()
                if (result?.data != null){
                    adapter.data.clear()
                    result.data.filter { marketName == it.quoteAsset }
                            .forEach { adapter.data.add(it) }
                    adapter.notifyDataSetChanged()
                    val mlr = MarketListener()
                    mlr.run()
                }
            }

            override fun onError(message: String?) {
                progress.dismiss()
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    inner class MarketListener : WebSocketListener() {

        fun run() {
            val request = Request.Builder()
                    .url("wss://stream.binance.com:9443/ws/bnbbtc@aggTrade")
                    .addHeader("Connection", "keep-alive")
                    .build()
            App.INSTANCE.httpClient.newWebSocket(request, this)
            App.INSTANCE.httpClient.dispatcher().executorService().shutdown()
        }

        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            webSocket?.send("")
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            Log.e("webss", t?.toString())
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            webSocket?.close(1000, null)
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            try {
                val trade = App.INSTANCE.gson.fromJson<Trade>(text, Trade::class.java)
                (mContext as BaseActivity).runOnUiThread(object : Runnable{
                    override fun run() {
                        val position = adapter.updateTrade(trade)
                        if (position >= 0){
                            adapter.notifyItemChanged(position)
                        }
                    }
                })
                Log.i("webss", trade.toString())
            }catch (e : Exception) {
                Log.e("webss", e.toString())
            }
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            Log.i("webss", bytes?.hex())
        }
    }

}