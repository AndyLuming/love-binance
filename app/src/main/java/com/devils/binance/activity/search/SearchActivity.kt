package com.devils.binance.activity.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.devils.binance.R
import com.devils.binance.base.BaseActivity
import com.devils.binance.bean.Product

class SearchActivity : BaseActivity() {

    private lateinit var editText : EditText
    private lateinit var recyclerView  : RecyclerView
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter : SearchAdapter

    private var data : ArrayList<Product> = ArrayList()

    override val layoutResId: Int
        get() = R.layout.activity_search
    override val tag: String
        get() = javaClass.simpleName

    override fun setUpViews(savedInstanceState: Bundle?) {
        findViewById<View>(R.id.cancel).setOnClickListener{ finish() }
        editText = findViewById(R.id.editSearch)
        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateSearchResult(p0?.toString())
            }
        })
        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this@SearchActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = SearchAdapter(this@SearchActivity)
        recyclerView.adapter = adapter

        editText.requestFocus()
    }

    override fun work(savedInstanceState: Bundle?) {
        if (intent != null) {
            try {
                data = intent.getSerializableExtra("data") as ArrayList<Product>
            } catch (e : ClassCastException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateSearchResult(keyword : String?){
        if (keyword.isNullOrBlank()) {
            adapter.data.clear()
        }else {
            adapter.data = data.filter {
                it.symbol.toLowerCase().contains(keyword!!.toLowerCase())
            } as ArrayList<Product>
        }
        adapter.notifyDataSetChanged()
    }

}
