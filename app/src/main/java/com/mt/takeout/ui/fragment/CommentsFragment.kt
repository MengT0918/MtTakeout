package com.mt.takeout.ui.fragment

import android.view.View
import com.mt.takeout.R
import com.mt.takeout.base.BaseFragment

class CommentsFragment : BaseFragment() {
    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_comment, null)
    }
}