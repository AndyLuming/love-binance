package com.devils.binance.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AndyL on 2017/10/2.
 *
 */

public class Trade {


    /**
     * s : BNBBTC
     * p : 0.00009240
     * P : 30.036
     * w : 0.00035625
     * x : 0.00030761
     * c : 0.00040003
     * Q : 181.00000000
     * b : 0.00040008
     * B : 581.00000000
     * a : 0.00040198
     * A : 6.00000000
     * o : 0.00030763
     * h : 0.00043000
     * l : 0.00030238
     * v : 6956862.00000000
     * q : 2478.35286586
     * O : 1506839318873
     * C : 1506925718873
     * F : 1208679
     * L : 1235119
     * n : 26441
     */

    @Expose
    @SerializedName("s")
    private String symbol;
    @Expose
    @SerializedName("a")
    private String price;
    @Expose
    @SerializedName("p")
    private String priceChange;
    @Expose
    @SerializedName("P")
    private String percentChange;
    @Expose
    @SerializedName("h")
    private String high;
    @Expose
    @SerializedName("l")
    private String low;
    @Expose
    @SerializedName("q")
    private String quantity;

    public Trade() {
    }

    public Trade(String symbol, String price, String priceChange, String percentChange, String high, String low, String quantity) {
        this.symbol = symbol;
        this.price = price;
        this.priceChange = priceChange;
        this.percentChange = percentChange;
        this.high = high;
        this.low = low;
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
