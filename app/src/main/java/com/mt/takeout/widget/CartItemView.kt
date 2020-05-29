package com.mt.takeout.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.j256.ormlite.stmt.query.In
import com.mt.takeout.R
import com.mt.takeout.model.bean.GoodsInfo
import com.mt.takeout.ui.activity.BusinessActivity
import com.mt.takeout.ui.fragment.GoodsFragment
import com.mt.takeout.utils.PriceFormat
import kotlinx.android.synthetic.main.item_cart.view.*

class CartItemView : RelativeLayout, View.OnClickListener {
    private lateinit var mGoodsInfo: GoodsInfo
    private lateinit var mGoodsFragment: GoodsFragment

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_cart, this)
        item_cart_add.setOnClickListener(this)
        item_cart_minus.setOnClickListener(this)
        mGoodsFragment = (context as BusinessActivity).mFragments[0] as GoodsFragment
    }

    fun bindData(goodsInfo: GoodsInfo) {
        mGoodsInfo = goodsInfo
        item_cart_name.text = goodsInfo.name
        item_cart_price.text = PriceFormat.format(goodsInfo.newPrice.toFloat() * goodsInfo.count)
        item_cart_count.text = goodsInfo.count.toString()
    }

    override fun onClick(view: View?) {
        var isAdd = false
        when(view?.id) {
            R.id.item_cart_add -> {
                isAdd = true
                doAddOperation()
            }
            R.id.item_cart_minus -> doMinusOperation()
        }
        processRedDotCount(isAdd)
        (context as BusinessActivity).updateCartUi()
    }

    private fun doAddOperation() {
        mGoodsInfo.count++
        refreshListener?.let { it() }
        mGoodsFragment.mAllGoodsAdapter.notifyDataSetChanged()
    }

    private fun doMinusOperation() {
        var count = mGoodsInfo.count
        if (count == 1) {
            removeListener?.let {
                val size = it(mGoodsInfo)
                if (size == 0) {
                    (context as BusinessActivity).showOrHideCart()
                }
            }
        }
        count--
        mGoodsInfo.count = count
        refreshListener?.let { it() }
        mGoodsFragment.mAllGoodsAdapter.notifyDataSetChanged()
    }

    private fun processRedDotCount(isAdd: Boolean) {
        val typeId = mGoodsInfo.typeId
        val typePosition = mGoodsFragment.mPresenter.getTypePositionByTypeId(typeId)
        val goodsType = mGoodsFragment.mPresenter.goodsTypes[typePosition]
        var dotCount = goodsType.dotCount
        if (isAdd) {
            dotCount++
        } else {
            dotCount--
        }
        goodsType.dotCount = dotCount
        mGoodsFragment.mGoodsTypeAdapter.notifyItemChanged(typePosition)
    }

    //回调到Adapter里面，进行界面刷新操作
    private var refreshListener: (() -> Unit)? = null
    fun setRefreshListener(listener: (() -> Unit)) {
        refreshListener = listener
    }

    //回调到Adapter里面，进行界面刷新操作
    private var removeListener: ((goodsInfo: GoodsInfo) -> Int)? = null
    fun setRemoveListener(listener: (goodsInfo: GoodsInfo) -> Int) {
        removeListener = listener
    }
}