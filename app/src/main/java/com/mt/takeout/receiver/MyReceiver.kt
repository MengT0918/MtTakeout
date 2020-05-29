package com.mt.takeout.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cn.jpush.android.api.JPushInterface
import com.mt.takeout.utils.OrderObservable

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val bundle = it.extras
            val values = bundle?.getString(JPushInterface.EXTRA_EXTRA)
            OrderObservable.INSTANCE.sentMessage(values)
        }
    }
}