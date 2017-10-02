package com.devils.binance.activity.main

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devils.binance.R
import com.devils.binance.activity.trade.TradeActivity
import com.devils.binance.bean.Product
import com.devils.binance.bean.Trade
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

    private var symbols : SparseArray<String> = SparseArray()
    private var productsData   : HashMap<String, Product> = HashMap()

    override fun getItemCount(): Int {
        return symbols.size()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as MyViewHolder
        val mData = productsData[symbols[position]] ?: return
        if (position % 2 == 0){
            vh.itemBg?.setBackgroundColor(mContext.resources.getColor(R.color.bg_item_black))
        }else{
            vh.itemBg?.setBackgroundColor(mContext.resources.getColor(R.color.bg_item_gray))
        }

        if (mData.latestTrade == null) {

            vh.symbol?.text = mData.symbol
            vh.price?.text = mData.close.toString()

            val tmt = dm.format(mData.tradedMoney) + " " + mMarket
            vh.tradeAmount?.text = tmt

            val bdClose = BigDecimal(mData.close)
            val bdOpen = BigDecimal(mData.open)

            val change = bdClose.subtract(bdOpen)

            var placeholder = ""
            if (change.toDouble() < 0) {
                vh.changeAmount?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
                vh.changePercent?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
            } else if (change.toDouble() > 0){
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
            }
        }else{

            vh.symbol?.text = mData.symbol
            vh.price?.text = BigDecimal(mData.latestTrade.price).toPlainString()

            if (mData.latestTrade.priceChange.toDouble() >= 0) {
                val text = "+" + mData.latestTrade.priceChange
                vh.changeAmount?.text = text
            }else{
                vh.changeAmount?.text = mData.latestTrade.priceChange
            }
            if (mData.latestTrade.percentChange.toDouble() >= 0) {
                val text = "+" + dm.format(mData.latestTrade.percentChange.toDouble()) + "%"
                vh.changePercent?.text =  text
            }else{
                val text = dm.format(mData.latestTrade.percentChange.toDouble()) + "%"
                vh.changePercent?.text =  text
            }
            val amountStr = dm.format(mData.latestTrade.quantity.toDouble()) + " " + mMarket
            vh.tradeAmount?.text = amountStr

            if (mData.latestTrade.percentChange.toDouble() >= 0){
                vh.changeAmount?.setTextColor(mContext.resources.getColor(R.color.color_opt_gt))
                vh.changePercent?.setTextColor(mContext.resources.getColor(R.color.color_opt_gt))
            }else{
                vh.changeAmount?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
                vh.changePercent?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
            }

            if (!mData.isUpdate) {
                try {
                    when {
                        mData.latestTrade.price.toDouble() > mData.close.toDouble() -> {
                            vh.price?.setTextColor(mContext.resources.getColor(R.color.color_opt_gt))
                            vh.price?.postDelayed({
                                vh.price?.setTextColor(mContext.resources.getColor(R.color.white))
                            }, 1000)
                        }
                        mData.latestTrade.price.toDouble() < mData.close.toDouble() -> {
                            vh.price?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
                            vh.price?.postDelayed({
                                vh.price?.setTextColor(mContext.resources.getColor(R.color.white))
                            }, 1000)
                        }
                        else -> vh.price?.setTextColor(mContext.resources.getColor(R.color.white))
                    }

                } catch (e: Exception) {
                    Log.e(MarketAdapter::javaClass.name, e.toString())
                }
                mData.isUpdate = true
            }

            mData.close = mData.latestTrade.price
        }
        vh.itemView.setOnClickListener{
            val intent = Intent(mContext, TradeActivity::class.java)
            intent.putExtra("symbol", mData.symbol)
            intent.putExtra("market", mMarket)
            mContext.startActivity(intent)
        }
    }

    fun updateTrade(trades: List<Trade>?) : Int{
        if (trades != null && trades.isNotEmpty()){
            trades.forEach({
                productsData[it.symbol]?.latestTrade = it
                productsData[it.symbol]?.isUpdate = false
            })
        }

        return -1
    }

    fun setDataList(products : List<Product>?){
        if (products != null && products.isNotEmpty()){
            symbols.clear()
            productsData.clear()
            for ((index, prod) in products.withIndex()) {
                symbols.put(index, prod.symbol)
                productsData.put(prod.symbol, prod)
            }
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