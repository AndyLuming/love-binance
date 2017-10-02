package com.devils.binance.net.model;

import com.devils.binance.bean.Trade;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AndyL on 2017/10/2.
 *
 */

public class TradeList {

    @Expose
    @SerializedName("")
    private List<Trade> trades;

    public TradeList() {
    }

    public TradeList(List<Trade> trades) {
        this.trades = trades;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}
