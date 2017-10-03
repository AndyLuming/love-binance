package com.devils.binance.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by AndyL on 2017/10/1.
 *
 */

public class Product implements Serializable{

    @Expose
    @SerializedName("quoteAsset")
    private String quoteAsset;
    @Expose
    @SerializedName("activeBuy")
    private float activeBuy;
    @Expose
    @SerializedName("prevClose")
    private String prevClose;
    @Expose
    @SerializedName("symbol")
    private String symbol;
    @Expose
    @SerializedName("withdrawFee")
    private String withdrawFee;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("minQty")
    private String minQty;
    @Expose
    @SerializedName("minTrade")
    private String minTrade;
    @Expose
    @SerializedName("baseAssetUnit")
    private String baseAssetUnit;
    @Expose
    @SerializedName("quoteAssetUnit")
    private String quoteAssetUnit;
    @Expose
    @SerializedName("activeSell")
    private float activeSell;
    @Expose
    @SerializedName("lastAggTradeId")
    private int lastAggTradeId;
    @Expose
    @SerializedName("close")
    private String close;
    @Expose
    @SerializedName("decimalPlaces")
    private int decimalPlaces;
    @Expose
    @SerializedName("open")
    private String open;
    @Expose
    @SerializedName("baseAsset")
    private String baseAsset;
    @Expose
    @SerializedName("volume")
    private String volume;
    @Expose
    @SerializedName("active")
    private boolean active;
    @Expose
    @SerializedName("tradedMoney")
    private float tradedMoney;
    @Expose
    @SerializedName("high")
    private String high;
    @Expose
    @SerializedName("low")
    private String low;
    @Expose
    @SerializedName("tickSize")
    private float tickSize;
    @Expose
    @SerializedName("matchingUnitType")
    private String matchingUnitType;

    private String lastClose;

    private Trade  latestTrade;

    private boolean update;

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public double getActiveBuy() {
        return activeBuy;
    }

    public void setActiveBuy(int activeBuy) {
        this.activeBuy = activeBuy;
    }

    public String getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(String prevClose) {
        this.prevClose = prevClose;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(String withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMinQty() {
        return minQty;
    }

    public void setMinQty(String minQty) {
        this.minQty = minQty;
    }

    public String getMinTrade() {
        return minTrade;
    }

    public void setMinTrade(String minTrade) {
        this.minTrade = minTrade;
    }

    public String getBaseAssetUnit() {
        return baseAssetUnit;
    }

    public void setBaseAssetUnit(String baseAssetUnit) {
        this.baseAssetUnit = baseAssetUnit;
    }

    public String getQuoteAssetUnit() {
        return quoteAssetUnit;
    }

    public void setQuoteAssetUnit(String quoteAssetUnit) {
        this.quoteAssetUnit = quoteAssetUnit;
    }

    public double getActiveSell() {
        return activeSell;
    }

    public void setActiveSell(int activeSell) {
        this.activeSell = activeSell;
    }

    public int getLastAggTradeId() {
        return lastAggTradeId;
    }

    public void setLastAggTradeId(int lastAggTradeId) {
        this.lastAggTradeId = lastAggTradeId;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getTradedMoney() {
        return tradedMoney;
    }

    public void setTradedMoney(float tradedMoney) {
        this.tradedMoney = tradedMoney;
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

    public double getTickSize() {
        return tickSize;
    }

    public void setTickSize(float tickSize) {
        this.tickSize = tickSize;
    }

    public String getMatchingUnitType() {
        return matchingUnitType;
    }

    public void setMatchingUnitType(String matchingUnitType) {
        this.matchingUnitType = matchingUnitType;
    }

    public String getLastClose() {
        return lastClose;
    }

    public void setLastClose(String lastClose) {
        this.lastClose = lastClose;
    }

    public Trade getLatestTrade() {
        return latestTrade;
    }

    public void setLatestTrade(Trade latestTrade) {
        this.latestTrade = latestTrade;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
