package com.mt.takeout.presenter.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mt.takeout.base.BaseNetPresenterImpl
import com.mt.takeout.model.bean.Order
import com.mt.takeout.presenter.interf.OrderPresenter
import com.mt.takeout.utils.ThreadUtil
import com.mt.takeout.view.OrderView

class OrderPresenterImpl(private val orderView: OrderView) : OrderPresenter, BaseNetPresenterImpl() {
    override fun loadOrder(userId: String) {
        val orderCall = mTakeoutService.getOrderList(userId)
        orderCall.enqueue(callback)
    }

    override fun parserJson(json: String) {
        val orders = Gson().fromJson<List<Order>>(json, object : TypeToken<List<Order>>() {}.type)
        if (orders.isNotEmpty()) {
            ThreadUtil.runOnMainThread(Runnable { orderView.onSuccess(orders, null) })
        } else {
            ThreadUtil.runOnMainThread(Runnable { orderView.onError("获取订单数据失败") })
        }
    }

    override fun responseFail() {
        ThreadUtil.runOnMainThread(Runnable { orderView.onError("服务器连接失败") })
    }
}