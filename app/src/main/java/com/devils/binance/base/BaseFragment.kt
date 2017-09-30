package com.ysst.consultant.base.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by andy on 17-9-30.
 *
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var mContext : Context
    protected var rootView: View? = null

    protected abstract val layoutResId : Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (rootView == null) {
            rootView = inflater.inflate(layoutResId, container, false)
            setUpViews(rootView!!, savedInstanceState)
            preWork(savedInstanceState)
        }

        work(savedInstanceState)

        return rootView!!
    }

    abstract fun setUpViews(root: View, savedInstanceState: Bundle?): Unit

    /**
     * work once when rootView is null
     * @see onCreateView
     * */
    abstract fun preWork(savedInstanceState: Bundle?): Unit

    abstract fun work(savedInstanceState: Bundle?): Unit

}