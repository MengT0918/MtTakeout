package com.mt.takeout.presenter.impl

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mt.takeout.base.BaseNetPresenterImpl
import com.mt.takeout.model.bean.Order
import com.mt.takeout.presenter.interf.OrderPresenter
import com.mt.takeout.utils.ThreadUtil
import com.mt.takeout.view.OrderView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OrderPresenterImpl(private val orderView: OrderView) : OrderPresenter, BaseNetPresenterImpl() {
    override fun loadOrder(userId: String) {
        //val orderCall = mTakeoutService.getOrderList(userId)
        //orderCall.enqueue(callback)
        val observable = mTakeoutService.getOrderListByRxJava(userId)
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                parserJson(it.data)
            }, {
                responseFail()
            }, {
                Log.e("rxJava", "onComplete!");
            })
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