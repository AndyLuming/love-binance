package com.devils.binance.activity.main

import android.os.Bundle
import android.widget.Toast
import com.devils.binance.R
import com.devils.binance.base.BaseActivity
import java.util.*

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
