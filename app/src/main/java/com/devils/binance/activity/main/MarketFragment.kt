package com.devils.binance.activity.main

import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.devils.binance.R
import com.devils.binance.data.ProductRespository
import com.devils.binance.net.NetCallback
import com.devils.binance.net.model.ProductsList
import com.ysst.consultant.base.fragment.BaseFragment

class MarketFragment : BaseFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_market

    var marketName = ""

    private lateinit var refreshLayout : PtrFrameLayout
    private lateinit var recyclerView  : RecyclerView
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter       : MarketAdapter

    private val repo = ProductRespository()

    override fun setUpViews(root: View, savedInstanceState: Bundle?) {
        with(root){
            refreshLayout = findViewById(R.id.refreshLayout)
            recyclerView  = findViewById(R.id.recyclerView)
            layoutManager = LinearLayoutManager(mContext)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = layoutManager
            adapter = MarketAdapter(mContext, marketName)
            recyclerView.adapter = adapter
        }
    }

    override fun preWork(savedInstanceState: Bundle?) {

    }

    override fun work(savedInstanceState: Bundle?) {
        repo.fetchData(object : NetCallback<ProductsList>{

            override fun onSuccess(result: ProductsList?) {
                if (result != null && result.data != null){
                    adapter.data.clear()
                    result.data.filter { marketName == it.quoteAsset }
                            .forEach { adapter.data.add(it) }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onError(message: String?) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
            }

        })
    }


}