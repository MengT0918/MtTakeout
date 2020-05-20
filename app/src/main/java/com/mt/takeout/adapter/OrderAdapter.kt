package com.mt.takeout.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mt.takeout.model.bean.Order
import com.mt.takeout.utils.OrderObservable
import com.mt.takeout.widget.LoadMoreView
import com.mt.takeout.widget.OrderItemView
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * 观察者模式中的观察者（继承Observer）
 */
class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderHolder>(), Observer {
    private var mOrders = ArrayList<Order>()

    companion object {
        const val TYPE_ORDER = 0
        const val TYPE_BOTTOM = 1
    }

    init {
        OrderObservable.INSTANCE.addObserver(this)//让观察者和被观察者建立绑定关系
    }

    class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun updateData(list: List<Order>?) {
        list?.let {
            mOrders.clear()
            mOrders.addAll(it)
            notifyDataSetChanged()
        }
    }

    /**
     * 观察者的响应
     */
    override fun update(observable: Observable?, data: Any?) {
        val jsonObj = JSONObject(data as String)
        val id = jsonObj.getString("orderId")
        val type = jsonObj.getString("type")
        var index = -1
        mOrders.forEach {
            if (it.id.equals(id)) {
                it.type = type
                index = mOrders.indexOf(it)
            }
        }
        //notifyDataSetChanged()
        //刷新单个条目
        if (index != -1) {
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return when(viewType) {
            TYPE_ORDER -> OrderHolder(OrderItemView(parent.context))
            TYPE_BOTTOM -> OrderHolder(LoadMoreView(parent.context))
            else -> OrderHolder(LoadMoreView(parent.context))
        }
    }

    override fun getItemCount(): Int {
        return mOrders.size + 1
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        //如果是最后一条 不需要刷新view
        if (position == mOrders.size) return
        (holder.itemView as OrderItemView).bindData(mOrders[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mOrders.size) TYPE_BOTTOM else TYPE_ORDER
    }
}