package com.devils.binance.net

import com.devils.binance.App

/**
 * Created by AndyL on 2017/10/1.
 *
 */
object NetInvoker {

    @JvmField val DOMAIN = "https://www.binance.com"

    private var apiList : Apis? = null

    private fun getApiList(app: App) : Apis {
        if (apiList == null){
            apiList = app.retrofit.create(Apis::class.java)
        }
        return apiList!!
    }

    fun callApis(app: App) = getApiList(app)

}