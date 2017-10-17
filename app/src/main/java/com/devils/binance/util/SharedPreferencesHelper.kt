package com.devils.binance.util

import android.app.Activity
import android.content.Context

/**
 * Created by AndyL on 2017/10/17.
 *
 */

object SharedPreferencesHelper {

    val mode = Context.MODE_PRIVATE

    fun getStringSet(activity: Activity?, key : String) : Set<String>?{
        return activity?.getPreferences(mode)?.getStringSet(key, null)
    }

    fun putStringSet(activity : Activity?, key : String,  data : Set<String>) {
        activity?.apply {
            getPreferences(Context.MODE_PRIVATE).edit()
                    .putStringSet(key, data).apply() }
    }

}
