package com.devils.binance.net;

/**
 * Created by AndyL on 2017/10/1.
 *
 */

public interface NetCallback<T> {

    void onSuccess(T result);

    void onError(String message);

}
