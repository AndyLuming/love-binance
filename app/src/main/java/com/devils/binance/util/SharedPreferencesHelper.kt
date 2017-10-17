package com.devils.binance.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

/**
 * Created by AndyL on 2017/10/17.
 *
 */

object SharedPreferencesHelper {

    private val mode = Context.MODE_PRIVATE

    fun getStringSet(activity: Activity?, key : String) : Set<String>?{
        return activity?.getPreferences(mode)?.getStringSet(key, null)
    }

    @SuppressLint("ApplySharedPref")
    fun putStringSet(activity : Activity?, key : String, data : Set<String>) {
        activity?.apply {
            getPreferences(Context.MODE_PRIVATE).edit()
                    .putStringSet(key, data)
                    .commit() }
    }

    @SuppressLint("ApplySharedPref")
    fun clearStringSet(activity: Activity?, key: String) {
        activity?.getPreferences(mode)?.edit()?.remove(key)?.commit()
    }

}
