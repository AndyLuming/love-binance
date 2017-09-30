package com.devils.binance.activity.start

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.devils.binance.R
import com.devils.binance.activity.main.MainActivity
import com.devils.binance.base.BaseActivity

class StartActivity : BaseActivity() {

    override val layoutResId: Int
        get() = R.layout.activity_start
    override val tag: String
        get() = javaClass.simpleName

    override fun setUpViews(savedInstanceState: Bundle?) {

    }

    override fun work(savedInstanceState: Bundle?) {
        Handler().apply {
            postDelayed({
                val intent = Intent(this@StartActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }

}
