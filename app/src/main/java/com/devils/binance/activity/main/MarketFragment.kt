package com.devils.binance.activity.main

import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.devils.binance.R
import com.ysst.consultant.base.fragment.BaseFragment

class MarketFragment : BaseFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_market

    var marketName = ""

    private var refreshLayout : PtrFrameLayout? = null
    private var recyclerView  : RecyclerView?   = null
    private val layoutManager : LinearLayoutManager by lazy { LinearLayoutManager(mContext) }

    override fun setUpViews(root: View, savedInstanceState: Bundle?) {

    }

    override fun preWork(savedInstanceState: Bundle?) {

    }

    override fun work(savedInstanceState: Bundle?) {

    }


}