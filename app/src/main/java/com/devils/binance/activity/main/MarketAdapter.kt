package com.devils.binance.activity.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.devils.binance.R

/**
 * Created by andy on 2017/9/30.
 *
 */
class MarketAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mContext = context

    override fun getItemCount(): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    inner class MyViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.viewholder_market, parent, false)) {

    }
}