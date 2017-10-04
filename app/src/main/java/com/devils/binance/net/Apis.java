package com.devils.binance.net;

import com.devils.binance.bean.CnyUsd;
import com.devils.binance.net.model.ProductsList;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by AndyL on 2017/10/1.
 *
 */

public interface Apis {

    @GET("/exchange/public/product")
    Observable<Result<ProductsList>> fetchData();

    @GET("/exchange/public/cnyusd")
    Observable<Result<CnyUsd>> cnyUsd();

    @GET("/api/v1/klines")
    Observable<Result<List<List<String>>>> kLine(@Query("symbol") String symbol, @Query("interval") String interval);
}
