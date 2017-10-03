package com.devils.binance.activity.main

import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.devils.binance.R
import com.devils.binance.bean.Product
import com.ysst.consultant.base.fragment.BaseFragment

class MarketFragment : BaseFragment() {

    @JvmField val API_KEY = "awiuncpoijhvufnvjnvjkndfivfnjkvnfjfjuioio"
    @JvmField val SEC_KEY = "ajwhdiuahdaaoidjuwaijduiadhyuevfsbjncqwie"

    override val layoutResId: Int
        get() = R.layout.fragment_market

    var marketName = ""

    private lateinit var refreshLayout : PtrFrameLayout
    private lateinit var recyclerView  : RecyclerView
    private lateinit var layoutManager : LinearLayoutManager
    var adapter : MarketAdapter? = null
    var mRate = 0.0
    var marketUsdtRate = 0.0

    var data : ArrayList<Product> = ArrayList()

    override fun setUpViews(root: View, savedInstanceState: Bundle?) {
        with(root){
            refreshLayout = findViewById(R.id.refreshLayout)
            recyclerView  = findViewById(R.id.recyclerView)
            layoutManager = LinearLayoutManager(mContext)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = layoutManager
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapter = MarketAdapter(mContext, marketName)
            recyclerView.adapter = adapter
        }
    }

    override fun preWork(savedInstanceState: Bundle?) {

    }

    override fun work(savedInstanceState: Bundle?) {
        adapter?.setDataList(data)
        adapter?.cnyUsdRate = mRate
        adapter?.marketUsdtRate = marketUsdtRate
        adapter?.notifyDataSetChanged()
    }

}