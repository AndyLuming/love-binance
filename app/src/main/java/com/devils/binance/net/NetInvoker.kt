package com.devils.binance.net

import com.devils.binance.App

/**
 * Created by AndyL on 2017/10/1.
 *
 */
object NetInvoker {

    @JvmField val DOMAIN = "https://www.binance.com"

    private val apiList : Apis by lazy {
        App.INSTANCE.retrofit.create(Apis::class.java)
    }

    fun callApis() = apiList

}