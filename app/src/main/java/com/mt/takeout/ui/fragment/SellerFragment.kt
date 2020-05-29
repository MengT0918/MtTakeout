package com.mt.takeout.ui.fragment

import android.view.View
import com.mt.takeout.R
import com.mt.takeout.base.BaseFragment

class SellerFragment : BaseFragment() {
    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_business, null)
    }
}