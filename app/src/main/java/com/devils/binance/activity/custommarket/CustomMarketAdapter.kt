package com.devils.binance.activity.custommarket

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devils.binance.R
import com.devils.binance.activity.main.MarketAdapter
import com.devils.binance.activity.trade.TradeActivity
import com.devils.binance.bean.Product
import com.devils.binance.bean.Trade
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

/**
 * Created by AndyL on 2017/10/3.
 *
 */
class CustomMarketAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mContext = context
    private val dm = DecimalFormat("#.##")

    var data = ArrayList<Product>()
    var cnyUsdRate = 0.0
    var btcUsdRate = 0.0
    var ethUsdRate = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MarketHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as MarketHolder
        val mData = data[position]

        if (position % 2 == 0){
            vh.itemBg?.setBackgroundColor(mContext.resources.getColor(R.color.bg_item_black))
        }else{
            vh.itemBg?.setBackgroundColor(mContext.resources.getColor(R.color.bg_item_gray))
        }

        if (mData.latestTrade == null) {

            vh.symbol?.text = mData.symbol
            if (mData.quoteAsset == "USDT") {
                vh.price?.text = dm.format(mData.close.toDouble())
            }else{
                vh.price?.text = mData.close.toString()
            }

            var cny = BigDecimal(mData.close).multiply(BigDecimal(cnyUsdRate))
            when (mData.quoteAsset) {
                "BTC" -> {cny = cny.multiply(BigDecimal(btcUsdRate))}
                "ETH" -> {cny = cny.multiply(BigDecimal(ethUsdRate))}
            }
            val cnyStr = "￥" + dm.format(cny)
            vh.cny?.text = cnyStr

            val tmt = dm.format(mData.tradedMoney) + " " + mData.quoteAsset
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

            if (mData.quoteAsset == "USDT") {
                val changeAmtStr = placeholder + dm.format(change.toDouble())
                vh.changeAmount?.text = changeAmtStr
            }else {
                val changeAmtStr = placeholder + change.toPlainString()
                vh.changeAmount?.text = changeAmtStr
            }

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

            if (mData.quoteAsset == "USDT") {
                vh.price?.text = dm.format(mData.latestTrade.price.toDouble())
            }else{
                vh.price?.text = mData.latestTrade.price
            }

            var cny = BigDecimal(mData.latestTrade.price).multiply(BigDecimal(cnyUsdRate))
            when (mData.quoteAsset) {
                "BTC" -> {cny = cny.multiply(BigDecimal(btcUsdRate))}
                "ETH" -> {cny = cny.multiply(BigDecimal(ethUsdRate))}
            }
            val cnyStr = "￥" + dm.format(cny)
            vh.cny?.text = cnyStr

            if (mData.latestTrade.priceChange.toDouble() >= 0) {
                if (mData.quoteAsset == "USDT"){
                    val text = "+" + dm.format(mData.latestTrade.priceChange.toDouble())
                    vh.changeAmount?.text = text
                }else {
                    val text = "+" + mData.latestTrade.priceChange
                    vh.changeAmount?.text = text
                }
            }else{
                if (mData.quoteAsset == "USDT"){
                    vh.changeAmount?.text = dm.format(mData.latestTrade.priceChange.toDouble())
                }else {
                    vh.changeAmount?.text = mData.latestTrade.priceChange
                }
            }

            if (mData.latestTrade.percentChange.toDouble() >= 0) {
                val text = "+" + dm.format(mData.latestTrade.percentChange.toDouble()) + "%"
                vh.changePercent?.text =  text
            }else{
                val text = dm.format(mData.latestTrade.percentChange.toDouble()) + "%"
                vh.changePercent?.text =  text
            }
            val amountStr = dm.format(mData.latestTrade.quantity.toDouble()) + " " + mData.quoteAsset
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
            intent.putExtra("market", mData.quoteAsset)
            mContext.startActivity(intent)
        }
    }

    fun updateTrade(trades: List<Trade>?) : Int{
        if (trades != null && trades.isNotEmpty()){
            trades.forEach({
                data.filter { datum -> it.symbol == datum.symbol }
                        .forEach { datum -> datum.latestTrade = it }
            })
        }

        return -1
    }

    inner class MarketHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.viewholder_market, parent, false)) {

        var itemBg           : LinearLayout? = null
        var symbol           : TextView?     = null
        var changeAmount     : TextView?     = null
        var changePercent    : TextView?     = null
        var price            : TextView?     = null
        var cny              : TextView? = null
        var tradeAmount      : TextView?     = null

        init {
            with(itemView){
                itemBg          = findViewById(R.id.marketItem)
                symbol          = findViewById(R.id.symbol)
                changeAmount    = findViewById(R.id.changeAmount)
                changePercent   = findViewById(R.id.changePercent)
                price           = findViewById(R.id.price)
                cny             = findViewById(R.id.cny)
                tradeAmount     = findViewById(R.id.tradeAmount)
            }
        }

    }

}