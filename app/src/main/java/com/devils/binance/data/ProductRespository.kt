package com.devils.binance.data

import com.devils.binance.net.NetCallback
import com.devils.binance.net.NetInvoker
import com.devils.binance.net.model.ProductsList
import io.reactivex.disposables.Disposable

/**
 * Created by AndyL on 2017/10/1.
 *
 */
class ProductRespository : BaseRepository(){

    fun fetchData(callback : NetCallback<ProductsList>) : Disposable {
        return invokeRemote(NetInvoker.callApis().fetchData(), callback)
    }

}