package com.mt.takeout.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.mt.takeout.R
import com.mt.takeout.model.bean.GoodsInfo
import com.mt.takeout.ui.activity.BusinessActivity
import com.mt.takeout.ui.fragment.GoodsFragment
import com.mt.takeout.utils.PriceFormat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_all_goods.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class AllGoodsItemView : RelativeLayout {
    private val goodsIcon by lazy { item_all_goods_icon }
    private val goodsName by lazy { item_all_goods_name }
    private val goodsFrom by lazy { item_all_goods_form }
    private val goodsMonthSale by lazy { item_all_goods_month_sale }
    private val goodsNewPrice by lazy { item_all_goods_new_price }
    private val goodsOldPrice by lazy { item_all_goods_old_price }
    private val goodsCount by lazy { item_all_goods_count }
    private val goodsMinus by lazy { item_all_goods_minus }
    private val goodsAdd by lazy { item_all_goods_add }
    private var goodsFragment: GoodsFragment? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object

    val DURATION = 1000L

    init {
        View.inflate(context, R.layout.item_all_goods, this)
    }

    fun setFragment(goodsFragment: GoodsFragment) {
        this.goodsFragment = goodsFragment
    }

    fun bindData(goodsInfo: GoodsInfo) {
        Picasso.with(context).load(goodsInfo.icon).into(goodsIcon)
        goodsName.text = goodsInfo.name
        goodsFrom.text = goodsInfo.form
        goodsMonthSale.text = "月售${goodsInfo.monthSaleNum}份"
        goodsNewPrice.text = PriceFormat.format(goodsInfo.newPrice.toFloat())
        if (goodsInfo.oldPrice > 0) {
            goodsOldPrice.visibility = View.VISIBLE
            goodsOldPrice.text = PriceFormat.format(goodsInfo.oldPrice.toFloat())
            goodsOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            goodsOldPrice.visibility = View.GONE
        }
        if (goodsInfo.count > 0) {
            goodsCount.visibility = View.VISIBLE
            goodsMinus.visibility = View.VISIBLE
        } else {
            goodsCount.visibility = View.INVISIBLE
            goodsMinus.visibility = View.INVISIBLE
        }
        goodsCount.text = goodsInfo.count.toString()

        goodsAdd.onClick {
            var count = goodsInfo.count
            if (count == 0) {
                val showAnimations: AnimationSet = getShowAnimations()
                goodsMinus.startAnimation(showAnimations)
                goodsCount.startAnimation(showAnimations)
            }
            count++
            goodsInfo.count = count
            refreshListener?.let {
                it()
            }
            //更新左边RecycleView的条目上TextView显示的个数
            processRedDotCount(goodsInfo, true)

            //抛物线
            //1、克隆+号，添加到Activity上
            /*var ib = ImageButton(context)
            ib.setBackgroundResource(R.drawable.button_add)
            val srcLocation = IntArray(2)
            goodsAdd.getLocationInWindow(srcLocation)
            ib.x = srcLocation[0].toFloat()
            ib.y = srcLocation[1].toFloat()
            (goodsFragment?.activity as BusinessActivity).addImageButton(ib, goodsAdd.width, goodsAdd.height)*/
            //2、执行抛物线动画（平移、垂直加速位移）
            /*val destLocation = (goodsFragment?.activity as BusinessActivity).getCartLocation()
            startParabolaAnimation(ib, srcLocation, destLocation)*/
            (goodsFragment?.activity as BusinessActivity).updateCartUi()
        }
        goodsMinus.onClick {
            var count = goodsInfo.count
            if (count == 1) {
                val hideAnimations: AnimationSet = getHideAnimations()
                goodsMinus.startAnimation(hideAnimations)
                goodsCount.startAnimation(hideAnimations)
            }
            count--
            goodsInfo.count = count
            refreshListener?.let {
                it()
            }
            //更新左边RecycleView的条目上TextView显示的个数
            processRedDotCount(goodsInfo, false)
            (goodsFragment?.activity as BusinessActivity).updateCartUi()
        }
    }

    //回调到Adapter里面，进行界面刷新操作
    private var refreshListener: (() -> Unit)? = null
    fun setRefreshListener(listener: (() -> Unit)) {
        refreshListener = listener
    }

    private fun getShowAnimations(): AnimationSet {
        val animations = AnimationSet(false)
        val alphaAnimation = AlphaAnimation(0f, 1f)
        val rotateAnimation = RotateAnimation(
            0f,
            720f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 2.0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        animations.addAnimation(alphaAnimation)
        animations.addAnimation(rotateAnimation)
        animations.addAnimation(translateAnimation)
        animations.duration = DURATION
        return animations
    }

    private fun getHideAnimations(): AnimationSet {
        val animations = AnimationSet(false)
        val alphaAnimation = AlphaAnimation(1f, 0f)
        val rotateAnimation = RotateAnimation(
            720f,
            0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 2.0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        animations.addAnimation(alphaAnimation)
        animations.addAnimation(rotateAnimation)
        animations.addAnimation(translateAnimation)
        animations.duration = DURATION
        return animations
    }

    private fun processRedDotCount(goodsInfo: GoodsInfo, isAdd: Boolean) {
        val typeId = goodsInfo.typeId
        goodsFragment?.let {
            val typePosition = it.mPresenter.getTypePositionByTypeId(typeId)
            val goodsType = it.mPresenter.goodsTypes[typePosition]
            var dotCount = goodsType.dotCount
            if (isAdd) {
                dotCount++
            } else {
                dotCount--
            }
            goodsType.dotCount = dotCount
            it.mGoodsTypeAdapter.notifyItemChanged(typePosition)
        }
    }

    private fun startParabolaAnimation(ib: ImageButton, srcLocation: IntArray, destLocation: IntArray) {
        val translateX = TranslateAnimation(
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, destLocation[0].toFloat() - srcLocation[0].toFloat(),
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f
        )
        val translateY = TranslateAnimation(
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, destLocation[1].toFloat() - srcLocation[1].toFloat()
        )
        translateY.interpolator = AccelerateInterpolator()
        val animationSet = AnimationSet(false)
        animationSet.addAnimation(translateX)
        animationSet.addAnimation(translateY)
        animationSet.duration = DURATION
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                val viewParent = ib.parent
                if (viewParent != null) {
                    (viewParent as ViewGroup).removeView(ib)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        ib.startAnimation(animationSet)
    }
}