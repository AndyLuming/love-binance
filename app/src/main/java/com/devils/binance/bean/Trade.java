package com.devils.binance.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AndyL on 2017/10/2.
 *
 */

public class Trade {

    @Expose
    @SerializedName("s")
    private String s;
    @Expose
    @SerializedName("p")
    private String p;

    public Trade() {
    }

    public Trade(String s, String p) {
        this.s = s;
        this.p = p;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "s='" + s + '\'' +
                ", p='" + p + '\'' +
                '}';
    }
}
