package com.mt.takeout.ui.fragment

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mt.takeout.R
import com.mt.takeout.adapter.OrderAdapter
import com.mt.takeout.base.BaseFragment
import com.mt.takeout.model.bean.Order
import com.mt.takeout.presenter.impl.OrderPresenterImpl
import com.mt.takeout.TakeoutApplication
import com.mt.takeout.view.OrderView
import kotlinx.android.synthetic.main.fragment_order.*
import org.jetbrains.anko.support.v4.toast

class OrderFragment : BaseFragment(), OrderView {
    private var mUserId = -1
    private val mAdapter by lazy { OrderAdapter() }
    private val mPresenter by lazy { OrderPresenterImpl(this) }

    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_order, null)
    }

    override fun initData() {
        order_rv.layoutManager = LinearLayoutManager(context)
        order_rv.adapter = mAdapter

        order_srl.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN)

        mUserId = TakeoutApplication.sUser.id
        if (isUserIdExists()) {
            mPresenter.loadOrder(mUserId.toString())
        }
    }

    override fun initListener() {
        order_srl.setOnRefreshListener {
            if (isUserIdExists()) {
                mPresenter.loadOrder(mUserId.toString())
            }
        }
    }

    override fun onError(msg: String) {
        order_srl.isRefreshing = false
        toast(msg)
    }

    override fun onSuccess(orderList: List<Order>?, otherList: List<Order>?) {
        order_srl.isRefreshing = false
        mAdapter.updateData(orderList)
    }

    private fun isUserIdExists(): Boolean {
        return if (mUserId == -1) {
            toast("请先登录，再查看订单")
            false
        } else {
            true
        }
    }
}