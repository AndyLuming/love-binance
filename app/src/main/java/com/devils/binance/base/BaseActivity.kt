package com.devils.binance.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


/**
 * Created by andy on 17-9-30.
 *
 */

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Return the activity layout resources id
     * This method will be called in
     * @see onCreate
     */
    protected abstract val layoutResId: Int

    protected abstract val tag: String

    /**
     * Set up views here
     * This method will be called in
     * @see onCreate
     * @see .getLayoutResId
     */
    protected abstract fun setUpViews(savedInstanceState: Bundle?)

    /**
     * Do some work here like network
     * This method will be called in
     * @see onCreate
     * @see setUpViews
     */
    protected abstract fun work(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        setUpViews(savedInstanceState)
        registermBroadCast()
        work(savedInstanceState)
    }

    /**
     * When the activity jumped from a uri,
     * this method can get the params in the uri
     */
    protected fun getUriParam(paramName: String): String {
        var value = ""
        if (intent != null && intent.data != null) {
            val uri = intent.data
            value = uri!!.getQueryParameter(paramName)
        }
        return value
    }

    override fun onDestroy() {
        unregistermBroadCast()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    /**
     * if you need to register broadCast, override this method.
     * It will be called at
     * @see onCreate
     * @see unregistermBroadCast
     */
    private fun registermBroadCast() {}

    /**
     * unregister broadCast, override this method.
     * It will be called at
     * @see onDestroy
     */
    private fun unregistermBroadCast() {}

}
