package com.devils.binance.activity.trade

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.devils.binance.R
import com.devils.binance.base.BaseActivity
import com.devils.binance.bean.Product
import com.devils.binance.data.dataRepository
import com.devils.binance.net.NetCallback
import com.devils.binance.net.model.ProductsList
import com.devils.binance.widgets.ProgressView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TradeActivity : BaseActivity() {

    @JvmField val Y_SCALE_FACTOR = "1000"

    private val repo = dataRepository()
    private val progress : ProgressView by lazy { ProgressView(this) }
    private var isLoading = false
    private var symbolText = ""
    private var market = ""
    private val dm = DecimalFormat("#.##")
    private val xLabelCache : HashMap<Float, String> = HashMap()

    override val layoutResId: Int
        get() = R.layout.activity_trade
    override val tag: String
        get() = javaClass.simpleName

    private var symbol : TextView?  = null
    private var price  : TextView?  = null
    private var percent: TextView?  = null
    private var amount : TextView?  = null
    private var high   : TextView?  = null
    private var low    : TextView?  = null
    private var kLine  : LineChart? = null
    private var progressBar : ProgressBar? = null

    override fun setUpViews(savedInstanceState: Bundle?) {
        findViewById<View>(R.id.back).setOnClickListener{finish()}
        symbol  = findViewById(R.id.symbol)
        price   = findViewById(R.id.price)
        percent = findViewById(R.id.percent)
        amount  = findViewById(R.id.amount)
        high    = findViewById(R.id.high)
        low     = findViewById(R.id.low)
        kLine   = findViewById(R.id.kLine)
        progressBar = findViewById(R.id.progressBar)

        progressBar?.visibility = View.GONE

        kLine?.isAutoScaleMinMaxEnabled = true
        kLine?.isScaleYEnabled = true
        kLine?.legend?.isEnabled = false
        kLine?.setDrawBorders(false)
        kLine?.setNoDataText("")
        kLine?.description = Description().apply { text = "" }

//        kLine?.setViewPortOffsets(-1f, 10f, 0f, 10f)

        kLine?.axisLeft?.textColor = resources.getColor(R.color.gray)
        kLine?.axisLeft?.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        kLine?.axisLeft?.setLabelCount(2, true)
        kLine?.axisLeft?.setDrawAxisLine(false)
        kLine?.axisLeft?.setDrawGridLines(false)
        kLine?.axisLeft?.textColor = resources.getColor(R.color.k_line_label_color)
        kLine?.axisLeft?.valueFormatter = IAxisValueFormatter {value, axis ->
            BigDecimal(value.toString()).div(BigDecimal(Y_SCALE_FACTOR)).toPlainString()
        }

        kLine?.axisRight?.setDrawLabels(false)
        kLine?.axisRight?.setDrawAxisLine(false)
        kLine?.axisRight?.setDrawGridLines(false)

        kLine?.xAxis?.axisLineColor = resources.getColor(R.color.k_line_label_color)
        kLine?.xAxis?.textColor = resources.getColor(R.color.k_line_label_color)
        kLine?.xAxis?.labelCount = 2
        kLine?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        kLine?.xAxis?.gridColor = resources.getColor(R.color.k_line_view_port)
        kLine?.xAxis?.valueFormatter = IAxisValueFormatter { value, axis ->
            val dateFormat = SimpleDateFormat("HH:mm", Locale.CHINA)
            try {
                dateFormat.format(xLabelCache[value]?.toLong())
            } catch (e : Exception){
                e.printStackTrace()
            }
            value.toString()
        }
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
                    fetchKLineData(symbolText, "1m")
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

    private fun fetchKLineData(symbol : String, interval : String) {
        if (isLoading) return
        isLoading = true
        progressBar?.visibility = View.VISIBLE
        repo.kLines(symbol, interval, object : NetCallback<List<List<String>>>{
            override fun onSuccess(result: List<List<String>>?) {
                isLoading = false
                progressBar?.visibility = View.GONE
                if (result != null){
                    val entriesMax = ArrayList<Entry>() //todo open out the max scale y
                    val entriesMin = ArrayList<Entry>() //todo open out the min scale y

                    val entries = ArrayList<Entry>()  //k data
                    val entries2 = ArrayList<Entry>() //average data
                    var count = 1
                    var sum = 0f
                    var avl = 0f

                    xLabelCache.clear()
                    result.forEach {
                        val x1 = count.toFloat()
                        val y1 = (BigDecimal(it[4])).multiply(BigDecimal(Y_SCALE_FACTOR)).toFloat()
                        entries.add(Entry(x1, y1))

                        sum += y1
                        avl = sum / count

                        if (it[7].toDouble() > 0 && it[5].toDouble() > 0) {
                            val sub = BigDecimal(it[7]).div(BigDecimal(it[5])).multiply(BigDecimal(Y_SCALE_FACTOR)).toFloat()
                            avl = (avl + sub) / 2
                            entries2.add(Entry(x1, avl))
                        }

                        Log.i("entries2", avl.toString())


                        entriesMax.add(Entry(x1, y1 * 1.005f))
                        entriesMin.add(Entry(x1, y1 * 0.995f))

//                        if (it[7].toDouble() > 0 && it[5].toDouble() > 0) {
//                            val y2 = BigDecimal(it[7]).div(BigDecimal(it[5])).multiply(BigDecimal(Y_SCALE_FACTOR)).toFloat()
//                            entries2.add(Entry(x1, y2))
//                        }

                        xLabelCache.put(x1, it[6])
                        count += 1
                    }
                    val dataSet = LineDataSet(entries, "")
                    //set color
                    dataSet.setDrawValues(false)
                    dataSet.setDrawCircles(false)
                    dataSet.color = resources.getColor(R.color.k_line_blue)

                    val dataSet2 = LineDataSet(entries2, "")
                    dataSet2.setDrawValues(false)
                    dataSet2.setDrawCircles(false)
                    dataSet2.color = resources.getColor(R.color.k_line_yellow)

                    val dataMax = LineDataSet(entriesMax, "")
                    dataMax.setDrawValues(false)
                    dataMax.setDrawCircles(false)
                    dataMax.color = Color.TRANSPARENT

                    val dataMin = LineDataSet(entriesMin, "")
                    dataMin.setDrawValues(false)
                    dataMin.setDrawCircles(false)
                    dataMin.color = Color.TRANSPARENT

//                    val lineData = LineData(dataSet, dataSet2)
                    val lineData = LineData(dataSet, dataSet2, dataMax, dataMin)

                    kLine?.data = lineData
                    kLine?.setVisibleXRangeMaximum(60f)
                    kLine?.moveViewToX((count - 1).toFloat())
                    kLine?.invalidate()
                }
            }

            override fun onError(message: String?) {
                isLoading = false
                Toast.makeText(this@TradeActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}
