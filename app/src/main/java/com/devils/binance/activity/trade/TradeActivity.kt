package com.devils.binance.activity.trade

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.devils.binance.R
import com.devils.binance.base.BaseActivity
import com.devils.binance.bean.Product
import com.devils.binance.data.ProductRespository
import com.devils.binance.net.NetCallback
import com.devils.binance.net.model.ProductsList
import com.devils.binance.widgets.ProgressView
import java.math.BigDecimal
import java.text.DecimalFormat

class TradeActivity : BaseActivity() {

    private val repo = ProductRespository()
    private val progress : ProgressView by lazy { ProgressView(this) }
    private var isLoading = false
    private var symbolText = ""
    private var market = ""
    private val dm = DecimalFormat("#.##")

    override val layoutResId: Int
        get() = R.layout.activity_trade
    override val tag: String
        get() = javaClass.simpleName

    private var symbol : TextView? = null
    private var price  : TextView? = null
    private var percent: TextView? = null
    private var amount : TextView? = null
    private var high   : TextView? = null
    private var low    : TextView? = null

    override fun setUpViews(savedInstanceState: Bundle?) {
        findViewById<View>(R.id.back).setOnClickListener{finish()}
        symbol  = findViewById(R.id.symbol)
        price   = findViewById(R.id.price)
        percent = findViewById(R.id.percent)
        amount  = findViewById(R.id.amount)
        high    = findViewById(R.id.high)
        low     = findViewById(R.id.low)
    }

    override fun work(savedInstanceState: Bundle?) {
        if (intent != null){
            symbolText = intent.getStringExtra("symbol")
            market = intent.getStringExtra("market")
            if (!TextUtils.isEmpty(symbolText) && !TextUtils.isEmpty(market)){
                if (symbolText.contains(market)){
                    val symbolStart = symbolText.replace(market, "")
                    val title = symbolStart + " / " + market
                    symbol?.text = title
                }
            }
        }

        if (isLoading) return
        isLoading = true
        progress.show()
        repo.fetchData(object : NetCallback<ProductsList> {

            override fun onSuccess(result: ProductsList?) {
                progress.dismiss()
                isLoading = false
                if (result?.data != null) {
                    result.data.filter { it.symbol == symbolText }
                            .let { if (it.isNotEmpty()) updateView(it[0]) }
                }
            }

            override fun onError(message: String?) {
                progress.dismiss()
                isLoading = false
                Toast.makeText(this@TradeActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateView(product: Product){
        price?.text = product.close

        val tmt = dm.format(product.tradedMoney) + " " + market
        amount?.text = tmt

        val bdClose = BigDecimal(product.close)
        val bdOpen = BigDecimal(product.open)

        val change = bdClose.subtract(bdOpen)

        if (change.toDouble() < 0) {
            percent?.setTextColor(resources.getColor(R.color.color_opt_lt))
        } else if (change.toDouble() > 0){
            percent?.setTextColor(resources.getColor(R.color.color_opt_gt))
        }

        high?.text = product.high
        low?.text = product.low

        val bdPrevClose = BigDecimal(product.prevClose)
        if (bdPrevClose.toDouble() > 0) {
            val changePct = change.div(bdPrevClose).multiply(BigDecimal("100"))
            var pctStr = dm.format(changePct) + "%"
            if (changePct.toDouble() > 0) {
                pctStr = "+" + pctStr
            }
            percent?.text = pctStr
        }
    }

}
