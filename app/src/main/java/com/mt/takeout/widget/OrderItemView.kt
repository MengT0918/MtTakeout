package com.mt.takeout.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mt.takeout.R
import com.mt.takeout.model.bean.Order
import com.mt.takeout.utils.OrderObservable
import kotlinx.android.synthetic.main.item_order.view.*

class OrderItemView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_order, this)
    }

    fun bindData(data: Order) {
        order_item_seller_name.text = data.seller?.name
        order_item_type.text = OrderObservable.INSTANCE.getOrderTypeInfo(data.type!!)
    }
}