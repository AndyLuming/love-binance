package com.devils.binance.activity.main

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.devils.binance.R
import com.devils.binance.base.BaseActivity
import java.util.*

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
        replaceMarket("BTC")
    }

    private fun replaceMarket(market : String){
        supportFragmentManager.beginTransaction().replace(R.id.marketContainer,
                MarketFragment().apply {
                    marketName = market
                }).commit()
    }

    private fun setCurrentTab(position : Int){

        when(position){
            0 -> {
                resetTabs()
                tabBtcTitle?.setTextColor(resources.getColor(R.color.colorAccent))
                tabBtcIndicator?.visibility = View.VISIBLE
            }
            1 -> {
                resetTabs()
                tabEthTitle?.setTextColor(resources.getColor(R.color.colorAccent))
                tabEthIndicator?.visibility = View.VISIBLE
            }
            2 -> {
                resetTabs()
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
}
