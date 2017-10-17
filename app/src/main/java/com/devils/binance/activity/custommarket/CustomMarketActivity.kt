package com.devils.binance.activity.custommarket

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.devils.binance.App
import com.devils.binance.R
import com.devils.binance.activity.custom.CustomActivity
import com.devils.binance.base.BaseActivity
import com.devils.binance.bean.Product
import com.devils.binance.bean.Trade
import com.devils.binance.common.Constants
import com.devils.binance.util.SharedPreferencesHelper
import com.google.gson.reflect.TypeToken
import okhttp3.WebSocket

class CustomMarketActivity : BaseActivity() {

    companion object {
        @JvmField val REQUEST_FOR_CUSTOM_MARKET = 9527
    }

    private lateinit var recyclerView : RecyclerView
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter : CustomMarketAdapter

    private var mData : ArrayList<Product> = ArrayList()
    private var mNowCustomMarket = HashMap<String, Boolean>()

    private var rate = 0.0

    private var btcUsdtRate = 0.0
    private var ethUsdtRate = 0.0

    private var mCustomMarketObserver : CustomMarketObserver? = null

    override val layoutResId: Int
        get() = R.layout.activity_custom_market
    override val tag: String
        get() = javaClass.simpleName

    override fun setUpViews(savedInstanceState: Bundle?) {
        findViewById<View>(R.id.back).setOnClickListener{ finish() }
        findViewById<View>(R.id.edit).setOnClickListener{
            val intent = Intent(this@CustomMarketActivity, CustomActivity::class.java)
            intent.putExtra("data", mData)
            intent.putExtra("customData", mNowCustomMarket)
            startActivityForResult(intent, REQUEST_FOR_CUSTOM_MARKET)
        }
        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this@CustomMarketActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = CustomMarketAdapter(this@CustomMarketActivity)
        recyclerView.adapter = adapter
    }

    override fun work(savedInstanceState: Bundle?) {

        mCustomMarketObserver = CustomMarketObserver((application as App), "wss://stream2.binance.com:9443/ws/!ticker@arr",
                object : CustomMarketObserver.CustomMarketObserverWatcher {
                    override fun onReceiveMessage(webSocket: WebSocket?, text: String?) {
                        runOnUiThread {
                            try {
                                val listType = object : TypeToken<ArrayList<Trade>>() {}.type
                                val trade = (application as App).gson.fromJson<List<Trade>>(text, listType)
                                for (t in trade) {
                                    mData.filter { t.symbol == it.symbol }
                                            .forEach {
                                                it.latestTrade = t
                                                it.isUpdate = false
                                            }
                                }
                                adapter.data.clear()
                                mData.filter {
                                    mNowCustomMarket.containsKey(it.symbol) && mNowCustomMarket[it.symbol]!!
                                }.forEach {
                                    adapter.data.add(it)
                                }
                                adapter.notifyDataSetChanged()
                            } catch (e: Exception) {
                                Log.e("webss", e.toString())
                            }
                        }
                    }
                })

        val marketSet = SharedPreferencesHelper.getStringSet(this@CustomMarketActivity, Constants.CUSTOM)
        mNowCustomMarket.clear()
        marketSet?.forEach { mNowCustomMarket.put(it, true) }

        if (intent != null){
            try {
                rate = intent.getDoubleExtra("cnyRate", 0.0)
                btcUsdtRate = intent.getDoubleExtra("btcRate", 0.0)
                ethUsdtRate = intent.getDoubleExtra("ethRate", 0.0)
                mData = intent.getSerializableExtra("data") as ArrayList<Product>
                adapter.data.clear()
                mData.filter {
                    mNowCustomMarket.containsKey(it.symbol) && mNowCustomMarket[it.symbol]!!
                }.forEach {
                    adapter.data.add(it)
                }
                adapter.cnyUsdRate = rate
                adapter.btcUsdRate = btcUsdtRate
                adapter.ethUsdRate = ethUsdtRate
                adapter.notifyDataSetChanged()

                if (mCustomMarketObserver != null && !mCustomMarketObserver!!.isObserving) {
                    mCustomMarketObserver?.connect()
                }
            } catch (e : ClassCastException) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        if (mCustomMarketObserver != null && !mCustomMarketObserver!!.isObserving) {
            mCustomMarketObserver?.connect()
        }
        super.onResume()
    }

    override fun onStop() {
        mCustomMarketObserver?.disconnect()
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_FOR_CUSTOM_MARKET){
            if (data?.getSerializableExtra("customMarketData") != null){
                try{
                    mNowCustomMarket = data.getSerializableExtra("customMarketData") as HashMap<String, Boolean>
                    adapter.data.clear()
                    mData.filter {
                        mNowCustomMarket.containsKey(it.symbol) && mNowCustomMarket[it.symbol]!!
                    }.forEach {
                        adapter.data.add(it)
                    }
                    adapter.notifyDataSetChanged()

                    val customMarketSet = mutableSetOf<String>()

                    mNowCustomMarket.forEach{ if (it.value) customMarketSet.add(it.key)}

                    SharedPreferencesHelper.run {
                        clearStringSet(this@CustomMarketActivity,Constants.CUSTOM)
                        putStringSet(this@CustomMarketActivity, Constants.CUSTOM, customMarketSet)
                    }
                }catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
