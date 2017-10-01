package com.devils.binance.activity.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devils.binance.R
import com.devils.binance.bean.Product
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by andy on 2017/9/30.
 *
 */
class MarketAdapter(context: Context, market :String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mContext = context
    private val mMarket  = market
    private val dm = DecimalFormat("#.##")

    public  var data : ArrayList<Product> = ArrayList()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as MyViewHolder
        val mData = data[position]
        if (position % 2 == 0){
            vh.itemBg?.setBackgroundColor(mContext.resources.getColor(R.color.bg_item_black))
        }else{
            vh.itemBg?.setBackgroundColor(mContext.resources.getColor(R.color.bg_item_gray))
        }
        vh.symbol?.text = mData.symbol
        vh.price?.text  = mData.close.toString()
        val tmt = dm.format(mData.tradedMoney) + " " + mMarket
        vh.tradeAmount?.text = tmt

        val bdClose = BigDecimal(mData.close)
        val bdOpen  = BigDecimal(mData.open)

        val change = bdClose.subtract(bdOpen)

        Log.i("", change.toString())
        var placeholder = ""
        if (change.toDouble() < 0) {
            vh.changeAmount?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
            vh.changePercent?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
        } else {
            vh.changeAmount?.setTextColor(mContext.resources.getColor(R.color.color_opt_gt))
            vh.changePercent?.setTextColor(mContext.resources.getColor(R.color.color_opt_gt))
            placeholder = "+"
        }

        val changeAmtStr = placeholder + change.toPlainString()
        vh.changeAmount?.text = changeAmtStr

        val bdPrevClose = BigDecimal(mData.prevClose)
        if (bdPrevClose.toDouble() > 0) {
            val changePct = change.div(bdPrevClose).multiply(BigDecimal("100"))
            var pctStr = dm.format(changePct) + "%"
            if (changePct.toDouble() > 0) {
                pctStr = "+" + pctStr
            }
            vh.changePercent?.text = pctStr
        }else{

        }
    }

    inner class MyViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.viewholder_market, parent, false)) {

        public var itemBg           : LinearLayout? = null
        public var symbol           : TextView?     = null
        public var changeAmount     : TextView?     = null
        public var changePercent    : TextView?     = null
        public var price            : TextView?     = null
        public var tradeAmount      : TextView?     = null

        init {
            with(itemView){
                itemBg          = findViewById(R.id.marketItem)
                symbol          = findViewById(R.id.symbol)
                changeAmount    = findViewById(R.id.changeAmount)
                changePercent   = findViewById(R.id.changePercent)
                price           = findViewById(R.id.price)
                tradeAmount     = findViewById(R.id.tradeAmount)
            }
        }

    }
}