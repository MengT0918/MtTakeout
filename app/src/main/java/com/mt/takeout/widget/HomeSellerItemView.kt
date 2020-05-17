package com.mt.takeout.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.mt.takeout.R
import com.mt.takeout.model.bean.HomeSeller
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_home_seller.view.*

class HomeSellerItemView : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_home_seller, this)
    }

    fun bindData(homeSeller: HomeSeller) {
        Picasso.with(context).load(homeSeller.icon).into(item_home_seller_logo)
        item_home_title.text = homeSeller.name
        item_home_rating_bar.rating = homeSeller.score.toFloat()
        item_home_sale.text = homeSeller.sale
        item_home_send_price.text = "￥${homeSeller.sendPrice}起送/配送费￥${homeSeller.deliveryFee}"
        item_home_distance.text = homeSeller.distance
    }
}