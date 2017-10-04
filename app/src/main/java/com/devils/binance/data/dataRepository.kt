package com.devils.binance.data

import com.devils.binance.bean.CnyUsd
import com.devils.binance.net.NetCallback
import com.devils.binance.net.NetInvoker
import com.devils.binance.net.model.ProductsList
import io.reactivex.disposables.Disposable

/**
 * Created by AndyL on 2017/10/1.
 *
 */
class dataRepository : BaseRepository(){

    fun fetchData(callback : NetCallback<ProductsList>) : Disposable {
        return invokeRemote(NetInvoker.callApis().fetchData(), callback)
    }

    fun cnyUsd(callback: NetCallback<CnyUsd>) : Disposable {
        return invokeRemote(NetInvoker.callApis().cnyUsd(), callback)
    }

    fun kLines(symbol : String, interval : String, callback: NetCallback<List<List<String>>>) : Disposable {
        return invokeRemote(NetInvoker.callApis().kLine(symbol, interval), callback)
    }

}