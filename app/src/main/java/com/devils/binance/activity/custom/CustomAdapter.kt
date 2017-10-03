package com.devils.binance.activity.custom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.devils.binance.R
import com.devils.binance.bean.Product

/**
 * Created by AndyL on 2017/10/3.
 *
 */
class CustomAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mContext = context
    var data : ArrayList<Product> = ArrayList()
    var mCustomMarket = HashMap<String, Boolean>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as CustomViewHolder
        val mData = data[position]

        vh.symbol?.text = mData.symbol
        vh.switch?.isChecked = mCustomMarket[mData.symbol] != null && mCustomMarket[mData.symbol]!!
        vh.switch?.setOnCheckedChangeListener { p0, p1 ->
            if (vh.switch != null && vh.switch!!.isPressed) {
                mCustomMarket.put(mData.symbol, p1)
            }
        }
    }

    inner class CustomViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(mContext)
            .inflate(R.layout.viewholder_custom_market, parent, false)){

        var symbol : TextView? = null
        var switch : Switch?   = null

        init { with(itemView){
            symbol = findViewById(R.id.symbol)
            switch = findViewById(R.id.marketSwitch)
        } }
    }
}