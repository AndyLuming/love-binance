package com.devils.binance.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AndyL on 2017/10/3.
 *
 */

public class CnyUsd {


    /**
     * rate : 6.6414
     * success : true
     */

    @Expose
    @SerializedName("rate")
    private double rate;
    @Expose
    @SerializedName("success")
    private boolean success;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
