package com.mt.takeout.utils

import java.util.*

/**
 * 观察者模式中的被观察者（继承Observable）
 */
class OrderObservable private constructor() : Observable() {
    companion object {
        val INSTANCE: OrderObservable = OrderObservable()

        /**
         * 订单状态
         * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送达 5已送达 6 取消的订单
         */
        const val ORDERTYPE_UNPAYMENT = "10"
        const val ORDERTYPE_SUBMIT = "20"
        const val ORDERTYPE_RECEIVEORDER = "30"
        const val ORDERTYPE_DISTRIBUTION = "40"

        // 骑手状态：接单、取餐、送餐
        const val ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE = "43"
        const val ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL = "46"
        const val ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL = "48"
        const val ORDERTYPE_SERVED = "50"
        const val ORDERTYPE_CANCELLEDORDER = "60"
    }

    fun getOrderTypeInfo(type: String): String {
        return when (type) {
            ORDERTYPE_UNPAYMENT -> "未支付"
            ORDERTYPE_SUBMIT ->"已提交订单"
            ORDERTYPE_RECEIVEORDER -> "商家接单"
            ORDERTYPE_DISTRIBUTION -> "配送中"
            ORDERTYPE_SERVED -> "已送达"
            ORDERTYPE_CANCELLEDORDER -> "取消的订单"
            else -> ""
        }
    }

    fun sentMessage(values: String?) {
        values?.let {
            //从广播接收者后去最新消息
            //通知所有观察者
            setChanged()
            notifyObservers(values)
        }
    }
}