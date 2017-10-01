package com.devils.binance.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import com.devils.binance.R

/**
 * Created by AndyL on 2017/10/1.
 *
 */
class ProgressView(context: Context?) : Dialog(context, R.style.BinanceProgressDialog) {

    private var image : ImageView? = null
    private var frameAnimation : AnimationDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        image = findViewById(R.id.image)
        image?.setBackgroundResource(R.drawable.loading)
        frameAnimation = image?.background as AnimationDrawable
        frameAnimation?.start()
        setCanceledOnTouchOutside(false)
        setOnDismissListener { frameAnimation?.stop() }
    }


}