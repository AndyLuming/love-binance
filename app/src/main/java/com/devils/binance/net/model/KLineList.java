package com.devils.binance.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AndyL on 2017/10/4.
 *
 */

public class KLineList implements Serializable {

    private List<String> onekLine;

    public KLineList(List<String> onekLine) {
        this.onekLine = onekLine;
    }

    public List<String> getOnekLine() {
        return onekLine;
    }

    public void setOnekLine(List<String> onekLine) {
        this.onekLine = onekLine;
    }
}
