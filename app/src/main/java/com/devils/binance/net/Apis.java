package com.devils.binance.net;

import com.devils.binance.net.model.ProductsList;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;

/**
 * Created by AndyL on 2017/10/1.
 *
 */

public interface Apis {

    @GET("/exchange/public/product")
    Observable<Result<ProductsList>> fetchData();

}
