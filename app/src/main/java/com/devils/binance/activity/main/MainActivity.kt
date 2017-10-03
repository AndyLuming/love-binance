package com.devils.binance.activity.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.devils.binance.App
import com.devils.binance.R
import com.devils.binance.base.BaseActivity
import com.devils.binance.bean.CnyUsd
import com.devils.binance.bean.Product
import com.devils.binance.bean.Trade
import com.devils.binance.data.dataRepository
import com.devils.binance.net.NetCallback
import com.devils.binance.net.model.ProductsList
import com.devils.binance.widgets.ProgressView
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okio.ByteString
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    override val layoutResId: Int
        get() = R.layout.activity_main
    override val tag: String
        get() = javaClass.simpleName

    private var tabBtc : RelativeLayout? = null
    private var tabEth : RelativeLayout? = null
    private var tabUsdt : RelativeLayout? = null

    private var tabBtcIndicator : View? = null
    private var tabEthIndicator : View? = null
    private var tabUsdtIndicator : View? = null

    private var tabBtcTitle : TextView? = null
    private var tabEthTitle : TextView? = null
    private var tabUsdtTitle : TextView? = null

    private val progress : ProgressView by lazy { ProgressView(this@MainActivity) }

    private val dataRepo : dataRepository by lazy { dataRepository() }
    private var mMarketObserver : MarketObserver? = null

    private var isLoading = false

    private var currentTab = 0
    private var currentMarket = "BTC"

    private var dataCache : ArrayList<Product> = ArrayList()
    private var rate = 0.0

    private var btcUsdtRate = 0.0
    private var ethUsdtRate = 0.0

    override fun setUpViews(savedInstanceState: Bundle?) {
        tabBtc = findViewById(R.id.tabBtc)
        tabEth = findViewById(R.id.tabEth)
        tabUsdt = findViewById(R.id.tabUsdt)

        tabBtcIndicator = findViewById(R.id.tabBtcIndicator)
        tabEthIndicator = findViewById(R.id.tabEthIndicator)
        tabUsdtIndicator = findViewById(R.id.tabUsdtIndicator)

        tabBtcTitle = findViewById(R.id.tabBtcTitle)
        tabEthTitle = findViewById(R.id.tabEthTitle)
        tabUsdtTitle = findViewById(R.id.tabUsdtTitle)

        tabBtc?.setOnClickListener {
            setCurrentTab(0)
            replaceMarket("BTC")
        }
        tabEth?.setOnClickListener {
            setCurrentTab(1)
            replaceMarket("ETH")
        }
        tabUsdt?.setOnClickListener {
            setCurrentTab(2)
            replaceMarket("USDT")
        }
    }

    override fun work(savedInstanceState: Bundle?) {
        mMarketObserver = MarketObserver()
        setCurrentTab(currentTab)
        fetchCnyUsd()
    }

    private fun replaceMarket(market : String){
        currentMarket = market
        supportFragmentManager.beginTransaction().replace(R.id.marketContainer,
                MarketFragment().apply {
                    mRate = rate
                    marketName = market
                    when(market){
                        "BTC" -> {marketUsdtRate = btcUsdtRate}
                        "ETH" -> {marketUsdtRate = ethUsdtRate}
                    }
                    data = dataCache.filter { it.quoteAsset == market } as ArrayList<Product>
                }, market).commit()
    }

    private fun setCurrentTab(position : Int){
        currentTab = position
        when(position){
            0 -> {
                resetTabs()
                tabBtc?.setBackgroundColor(resources.getColor(R.color.bg_item_black))
                tabBtcTitle?.setTextColor(resources.getColor(R.color.colorAccent))
                tabBtcIndicator?.visibility = View.VISIBLE
            }
            1 -> {
                resetTabs()
                tabEth?.setBackgroundColor(resources.getColor(R.color.bg_item_black))
                tabEthTitle?.setTextColor(resources.getColor(R.color.colorAccent))
                tabEthIndicator?.visibility = View.VISIBLE
            }
            2 -> {
                resetTabs()
                tabUsdt?.setBackgroundColor(resources.getColor(R.color.bg_item_black))
                tabUsdtTitle?.setTextColor(resources.getColor(R.color.colorAccent))
                tabUsdtIndicator?.visibility = View.VISIBLE
            }
        }
    }

    private fun resetTabs(){
        tabBtcTitle?.setTextColor(resources.getColor(R.color.white))
        tabEthTitle?.setTextColor(resources.getColor(R.color.white))
        tabUsdtTitle?.setTextColor(resources.getColor(R.color.white))

        tabBtcIndicator?.visibility = View.GONE
        tabEthIndicator?.visibility = View.GONE
        tabUsdtIndicator?.visibility = View.GONE

        tabBtc?.setBackgroundColor(resources.getColor(R.color.bg_item_gray))
        tabEth?.setBackgroundColor(resources.getColor(R.color.bg_item_gray))
        tabUsdt?.setBackgroundColor(resources.getColor(R.color.bg_item_gray))
    }

    private fun fetchCnyUsd(){
        if (isLoading) return
        isLoading = true
        progress.show()
        dataRepo.cnyUsd(object : NetCallback<CnyUsd>{
            override fun onSuccess(result: CnyUsd?) {
                isLoading = false
                if (result?.rate != null) {
                    rate = result.rate
                    fetchData()
                }else{
                    progress.dismiss()
                    Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(message: String?) {
                isLoading = false
                progress.dismiss()
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchData(){
        if (isLoading) return
        isLoading = true
        dataRepo.fetchData(object : NetCallback<ProductsList>{
            override fun onSuccess(result: ProductsList?) {
                progress.dismiss()
                isLoading = false
                if (result?.data != null) {
                    dataCache = result.data as ArrayList<Product>
                    try {
                        btcUsdtRate = result.data.filter { it.symbol == "BTCUSDT" }[0].prevClose.toDouble()
                        ethUsdtRate = result.data.filter { it.symbol == "ETHUSDT" }[0].prevClose.toDouble()
                    } catch (e : IndexOutOfBoundsException){
                        e.printStackTrace()
                    }
                    when (currentTab) {
                        0 -> {
                            replaceMarket("BTC")
                        }
                        1 -> {
                            replaceMarket("ETH")
                        }
                        2 -> {
                            replaceMarket("USDT")
                        }
                    }
                    if (mMarketObserver != null && !mMarketObserver!!.isRunning) {
                        mMarketObserver?.connect()
                    }
                }
            }

            override fun onError(message: String?) {
                isLoading = false
                progress.dismiss()
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        if (mMarketObserver != null && !mMarketObserver!!.isRunning && !isLoading) {
            mMarketObserver?.connect()
        }
        super.onResume()
    }

    override fun onStop() {
        mMarketObserver?.disconnect()
        super.onStop()
    }

    private var mBackKeyPressed = false

    override fun onBackPressed() {
        if (!mBackKeyPressed){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true
            Timer().schedule(object : TimerTask(){
                override fun run() {
                    mBackKeyPressed = false
                }
            }, 2000)
        }else{
            this.finish()
            System.exit(0)
        }
    }

    inner class MarketObserver : WebSocketListener() {

        var isRunning = false
        var mOkHttpClient : OkHttpClient? = null
        var mWebSocket : WebSocket? = null

        fun connect() {
            Log.i("MarketObserver", "connect")
            val request = Request.Builder()
                    .url("wss://stream2.binance.com:9443/ws/!ticker@arr")
                    .addHeader("Connection", "keep-alive")
                    .build()
            mOkHttpClient = App.INSTANCE.createHttpClient()
            mWebSocket = mOkHttpClient?.newWebSocket(request, this)
            mOkHttpClient?.dispatcher()?.executorService()?.shutdown()
            isRunning = true
        }

        fun disconnect() {
            Log.i("MarketObserver", "disconnect")
            mOkHttpClient?.dispatcher()?.cancelAll()
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
            runOnUiThread {
                try {
                    val listType = object : TypeToken<ArrayList<Trade>>(){}.type
                    val trade = App.INSTANCE.gson.fromJson<List<Trade>>(text, listType)
                    for (t in trade) {
                        dataCache.filter { t.symbol == it.symbol }
                                .forEach { it.latestTrade = t }
                    }
                    val currentFragment = supportFragmentManager.findFragmentByTag(currentMarket)
                    if (currentFragment != null && currentFragment is MarketFragment) {
                        currentFragment.apply {
                            data = dataCache.filter { it.symbol == marketName } as ArrayList<Product>
                            adapter?.updateTrade(trade)
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }catch (e : Exception) {
                    Log.e("webss", e.toString())
                }
            }
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            Log.i("webss", bytes?.hex())
        }
    }
}
