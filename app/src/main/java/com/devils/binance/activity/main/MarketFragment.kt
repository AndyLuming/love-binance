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
import com.devils.binance.bean.Product
import com.devils.binance.bean.Trade
import com.devils.binance.data.ProductRespository
import com.devils.binance.net.NetCallback
import com.devils.binance.net.model.ProductsList
import com.devils.binance.widgets.ProgressView
import com.google.gson.reflect.TypeToken
import com.ysst.consultant.base.fragment.BaseFragment
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString






class MarketFragment : BaseFragment() {

    @JvmField val API_KEY = "awiuncpoijhvufnvjnvjkndfivfnjkvnfjfjuioio"
    @JvmField val SEC_KEY = "ajwhdiuahdaaoidjuwaijduiadhyuevfsbjncqwie"

    override val layoutResId: Int
        get() = R.layout.fragment_market

    var marketName = ""

    private lateinit var refreshLayout : PtrFrameLayout
    private lateinit var recyclerView  : RecyclerView
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter       : MarketAdapter

    private val progress : ProgressView by lazy { ProgressView(mContext) }
    private val repo = ProductRespository()

    private var mMarketListener : MarketListener? = null

    private var isLoading = false

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
        if (mMarketListener == null){
            mMarketListener = MarketListener()
        }
        if (isLoading) return
        isLoading = true
        progress.show()
        repo.fetchData(object : NetCallback<ProductsList>{

            override fun onSuccess(result: ProductsList?) {
                progress.dismiss()
                isLoading = false
                if (result?.data != null){
                    val filters = ArrayList<Product>()
                    result.data.filter { marketName == it.quoteAsset }
                            .forEach { filters.add(it) }
                    adapter.setDataList(filters)
                    adapter.notifyDataSetChanged()
                    if (mMarketListener != null && !mMarketListener!!.isRunning){
                        mMarketListener?.run()
                    }
                }
            }

            override fun onError(message: String?) {
                progress.dismiss()
                isLoading = false
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
            }

        })
    }

//    override fun onResume() {
//        super.onResume()
//        if (adapter.itemCount > 1 && !isLoading
//                && mMarketListener != null && !mMarketListener!!.isRunning){
//            mMarketListener?.run()
//        }
//    }
//
//    override fun onStop() {
//        mMarketListener?.stop()
//        super.onStop()
//    }

    inner class MarketListener : WebSocketListener() {

        var isRunning = false
        var mWebSocket : WebSocket? = null

        fun run() {
            Log.i("MarketListener", "run")
            val request = Request.Builder()
                    .url("wss://stream2.binance.com:9443/ws/!ticker@arr")
                    .addHeader("Connection", "keep-alive")
                    .build()
            mWebSocket = App.INSTANCE.httpClient.newWebSocket(request, this)
            App.INSTANCE.httpClient.dispatcher().executorService().shutdown()
            isRunning = true
        }

        fun stop() {
            Log.i("MarketListener", "stop")
            mWebSocket?.close(1000, "closed")
            mWebSocket?.cancel()
            isRunning = false
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
            Log.i("webss", text)
            try {
                val listType = object : TypeToken<ArrayList<Trade>>(){}.type
                val trade = App.INSTANCE.gson.fromJson<List<Trade>>(text, listType)
                (mContext as BaseActivity).runOnUiThread {
                    adapter.updateTrade(trade)
                    adapter.notifyDataSetChanged()
                }
            }catch (e : Exception) {
                Log.e("webss", e.toString())
            }
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            Log.i("webss", bytes?.hex())
        }
    }

}