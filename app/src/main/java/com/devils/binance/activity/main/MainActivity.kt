package com.devils.binance.activity.main

import android.os.Bundle
import com.devils.binance.R
import com.devils.binance.base.BaseActivity

class MainActivity : BaseActivity() {

    override val layoutResId: Int
        get() = R.layout.activity_main
    override val tag: String
        get() = javaClass.simpleName

    override fun setUpViews(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction().replace(R.id.marketContainer,
                MarketFragment().apply {
                    marketName = "BTC"
                }).commit()
    }

    override fun work(savedInstanceState: Bundle?) {

    }

}
