package com.devils.binance.activity.custom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.devils.binance.R
import com.devils.binance.base.BaseActivity
import com.devils.binance.bean.Product

class CustomActivity : BaseActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter : CustomAdapter

    private var mData : ArrayList<Product> = ArrayList()
    private var mNowCustomMarket = HashMap<String, Boolean>()

    override val layoutResId: Int
        get() = R.layout.activity_custom
    override val tag: String
        get() = javaClass.simpleName

    override fun setUpViews(savedInstanceState: Bundle?) {
        findViewById<View>(R.id.back).setOnClickListener{
            finish()
        }
        findViewById<View>(R.id.confirm).setOnClickListener{
            mNowCustomMarket = adapter.mCustomMarket
            val result = Intent()
            result.putExtra("customMarketData", mNowCustomMarket)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this@CustomActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = CustomAdapter(this@CustomActivity)
        recyclerView.adapter = adapter
    }

    override fun work(savedInstanceState: Bundle?) {
        if (intent != null){
            try {
                mData = intent.getSerializableExtra("data") as ArrayList<Product>
                mNowCustomMarket = intent.getSerializableExtra("customData") as HashMap<String, Boolean>
                adapter.mCustomMarket = mNowCustomMarket
                adapter.data = mData
                adapter.notifyDataSetChanged()
            } catch (e : ClassCastException) {
                e.printStackTrace()
            }
        }
    }

}
