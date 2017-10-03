package com.devils.binance.activity.search

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.devils.binance.R
import com.devils.binance.activity.trade.TradeActivity
import com.devils.binance.bean.Product
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by AndyL on 2017/10/3.
 *
 */
class SearchAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mContext = context
    private val dm = DecimalFormat("#.##")
    var data : ArrayList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResultViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as ResultViewHolder
        val mData = data[position]

        if (mData.latestTrade == null) {
            vh.symbol?.text = mData.symbol
            vh.price?.text = mData.close.toString()

            val bdClose = BigDecimal(mData.close)
            val bdOpen = BigDecimal(mData.open)

            val change = bdClose.subtract(bdOpen)

            if (change.toDouble() < 0) {
                vh.percent?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
            } else if (change.toDouble() > 0){
                vh.percent?.setTextColor(mContext.resources.getColor(R.color.color_opt_gt))
            }

            val bdPrevClose = BigDecimal(mData.prevClose)
            if (bdPrevClose.toDouble() > 0) {
                val changePct = change.div(bdPrevClose).multiply(BigDecimal("100"))
                var pctStr = dm.format(changePct) + "%"
                if (changePct.toDouble() > 0) {
                    pctStr = "+" + pctStr
                }
                vh.percent?.text = pctStr
            }
        }else{
            vh.symbol?.text = mData.symbol
            vh.price?.text = mData.latestTrade.price

            if (mData.latestTrade.percentChange.toDouble() >= 0){
                vh.percent?.setTextColor(mContext.resources.getColor(R.color.color_opt_gt))
            }else{
                vh.percent?.setTextColor(mContext.resources.getColor(R.color.color_opt_lt))
            }

            if (mData.latestTrade.percentChange.toDouble() >= 0) {
                val text = "+" + dm.format(mData.latestTrade.percentChange.toDouble()) + "%"
                vh.percent?.text =  text
            }else{
                val text = dm.format(mData.latestTrade.percentChange.toDouble()) + "%"
                vh.percent?.text =  text
            }
        }

        vh.itemView.setOnClickListener{
            val intent = Intent(mContext, TradeActivity::class.java)
            intent.putExtra("symbol", mData.symbol)
            when {
                mData.symbol.contains("BTC") -> intent.putExtra("market", "BTC")
                mData.symbol.contains("ETH") -> intent.putExtra("market", "ETH")
                mData.symbol.contains("USDT") -> intent.putExtra("market", "USDT")
            }
            mContext.startActivity(intent)
        }
    }

    inner class ResultViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(mContext)
            .inflate(R.layout.viewholder_search_result, parent, false)){

        var symbol  : TextView? = null
        var price   : TextView? = null
        var percent : TextView? = null

        init { with(itemView){
            symbol  = itemView.findViewById(R.id.symbol)
            price   = itemView.findViewById(R.id.price)
            percent = itemView.findViewById(R.id.percent)
        } }

    }

}