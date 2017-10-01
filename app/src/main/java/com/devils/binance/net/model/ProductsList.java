package com.devils.binance.net.model;

import com.devils.binance.bean.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AndyL on 2017/10/1.
 *
 */

public class ProductsList {

    @Expose
    @SerializedName("data")
    public List<Product> data;

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
