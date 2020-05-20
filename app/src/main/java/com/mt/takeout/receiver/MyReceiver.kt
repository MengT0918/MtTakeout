package com.mt.takeout.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.mt.takeout.utils.OrderObservable

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("mengteng","Order")
        intent?.let {
            val bundle = it.extras
            val values = bundle.getString(JPushInterface.EXTRA_EXTRA)
            OrderObservable.INSTANCE.sentMessage(values)
        }
    }
}