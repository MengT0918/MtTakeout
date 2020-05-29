package com.mt.takeout.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mt.takeout.R
import com.mt.takeout.adapter.BusinessAdapter
import com.mt.takeout.adapter.CartAdapter
import com.mt.takeout.base.BaseActivity
import com.mt.takeout.base.BaseFragment
import com.mt.takeout.ui.fragment.CommentsFragment
import com.mt.takeout.ui.fragment.GoodsFragment
import com.mt.takeout.ui.fragment.SellerFragment
import com.mt.takeout.utils.PriceFormat
import kotlinx.android.synthetic.main.activity_business.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick


class BusinessActivity : BaseActivity() {
    val mFragments = listOf<BaseFragment>(GoodsFragment(), SellerFragment(), CommentsFragment())
    private val mTitles = listOf<String>("商品", "商家", "评论")
    private var mBottomSheet: View? = null

    private val mViewPage by lazy { business_vp }
    private val mTabLayout by lazy { business_tabs }
    private val mAdapter by lazy { BusinessAdapter(mFragments, mTitles, supportFragmentManager) }

    private val mCartAdapter by lazy { CartAdapter(supportFragmentManager) }

    override fun getLayoutId(): Int {
        return R.layout.activity_business
    }

    override fun initData() {
        mViewPage.adapter = mAdapter
        mTabLayout.setupWithViewPager(mViewPage)
    }

    override fun initListener() {
        business_bottom.onClick {
            showOrHideCart()
        }
    }

    private fun clearRedDot(goodsFragment: GoodsFragment) {
        val goodsTypes = goodsFragment.mPresenter.goodsTypes
        goodsTypes.map { it.dotCount = 0 }
    }

    fun showOrHideCart() {
        if (mBottomSheet == null) {
            //加载要显示的布局
            mBottomSheet = LayoutInflater.from(this@BusinessActivity)
                .inflate(R.layout.cart_list, window.decorView as ViewGroup, false)
            val rvCart = mBottomSheet!!.find<RecyclerView>(R.id.cart_rv)
            val clearCart = mBottomSheet!!.find<TextView>(R.id.cart_clear)
            rvCart.layoutManager = LinearLayoutManager(this@BusinessActivity)
            rvCart.adapter = mCartAdapter
            clearCart.onClick {
                val builder = AlertDialog.Builder(this@BusinessActivity)
                builder.setTitle("确定都不吃了吗?")
                builder.setPositiveButton("是，都不吃了!") { dialog, whith ->
                    val goodsFragment = (mFragments[0] as GoodsFragment)
                    goodsFragment.mPresenter.clearCart()
                    mCartAdapter.notifyDataSetChanged()
                    showOrHideCart()
                    goodsFragment.mAllGoodsAdapter.notifyDataSetChanged()
                    clearRedDot(goodsFragment)
                    goodsFragment.mGoodsTypeAdapter.notifyDataSetChanged()
                    updateCartUi()
                }
                builder.setNegativeButton("不，我还要吃!") { dialog, whith ->
                }
                builder.show()
            }
        }
        //判断BottomSheetLayout内容是否显示
        if (business_bottomSheetLayout.isSheetShowing) {
            //关闭内容显示
            business_bottomSheetLayout.dismissSheet()
        } else {
            val carts= (mFragments[0] as GoodsFragment).mPresenter.getCartList()
            mCartAdapter.setCartList(carts)
            //显示BottomSheetLayout里面的内容
            if (carts.size > 0) {
                business_bottomSheetLayout.showWithSheetView(mBottomSheet)
            }
        }
    }

    //添加一个+号
    fun addImageButton(ib: ImageButton, width: Int, height: Int) {
        business_fl_container.addView(ib, width, height)
    }

    //获取imgCart的位置坐标
    fun getCartLocation(): IntArray {
        val destLocation = IntArray(2)
        business_imgCart.getLocationInWindow(destLocation)
        return destLocation
    }

    //刷新布局底部数据
    fun updateCartUi() {
        var count = 0
        var countPrice = 0.0f
        val carts= (mFragments[0] as GoodsFragment).mPresenter.getCartList()
        carts.forEach {
            count += it.count
            countPrice += it.count * it.newPrice.toFloat()
        }
        if (count > 0) {
            business_tvSelectNum.visibility = View.VISIBLE
            business_tvSelectNum.text = count.toString()
        } else {
            business_tvSelectNum.visibility = View.INVISIBLE
        }
        business_tvCountPrice.text = PriceFormat.format(countPrice)
    }
}