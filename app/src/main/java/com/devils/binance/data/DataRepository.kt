package com.devils.binance.data

import com.devils.binance.App
import com.devils.binance.bean.CnyUsd
import com.devils.binance.net.NetCallback
import com.devils.binance.net.NetInvoker
import com.devils.binance.net.model.ProductsList
import io.reactivex.disposables.Disposable

/**
 * Created by AndyL on 2017/10/1.
 *
 */
class DataRepository(app: App) : BaseRepository(app){

    fun fetchData(callback : NetCallback<ProductsList>) : Disposable {
        return invokeRemote(NetInvoker.callApis(mApp).fetchData(), callback)
    }

    fun cnyUsd(callback: NetCallback<CnyUsd>) : Disposable {
        return invokeRemote(NetInvoker.callApis(mApp).cnyUsd(), callback)
    }

    fun kLines(symbol : String, interval : String, callback: NetCallback<List<List<String>>>) : Disposable {
        return invokeRemote(NetInvoker.callApis(mApp).kLine(symbol, interval), callback)
    }

}