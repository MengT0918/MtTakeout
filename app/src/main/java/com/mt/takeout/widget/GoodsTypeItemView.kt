package com.mt.takeout.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mt.takeout.R
import com.mt.takeout.model.bean.GoodsTypeInfo
import kotlinx.android.synthetic.main.item_goods_type.view.*

class GoodsTypeItemView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_goods_type, this)
    }

    fun bindData(goodsType: GoodsTypeInfo, selectPosition: Int, position: Int) {
        if (selectPosition == position) {
            //选中的为白底加粗黑字
            item_goods_type_ll.isSelected = true
            item_goods_type_tv.setTextColor(Color.BLACK)
            item_goods_type_tv.typeface = Typeface.DEFAULT_BOLD
        } else {
            //未选中是灰色背景 普通字体
            item_goods_type_ll.isSelected = false
            item_goods_type_tv.setTextColor(Color.GRAY)
            item_goods_type_tv.typeface = Typeface.DEFAULT
        }
        item_goods_type_tv.text = goodsType.name
        item_goods_type_tvRedDotCount.text = goodsType.dotCount.toString()
        if (goodsType.dotCount > 0) {
            item_goods_type_tvRedDotCount.visibility = View.VISIBLE
        } else {
            item_goods_type_tvRedDotCount.visibility = View.INVISIBLE
        }
    }
}