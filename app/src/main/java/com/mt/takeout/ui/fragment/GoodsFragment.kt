package com.mt.takeout.ui.fragment

import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mt.takeout.R
import com.mt.takeout.adapter.AllGoodsAdapter
import com.mt.takeout.adapter.GoodsTypeAdapter
import com.mt.takeout.base.BaseFragment
import com.mt.takeout.model.bean.GoodsInfo
import com.mt.takeout.model.bean.GoodsTypeInfo
import com.mt.takeout.presenter.impl.GoodsPresenterImpl
import com.mt.takeout.view.GoodsView
import kotlinx.android.synthetic.main.fragment_goods.*
import org.jetbrains.anko.support.v4.toast
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

class GoodsFragment : BaseFragment(), GoodsView {
    val mGoodsTypeAdapter by lazy { GoodsTypeAdapter(this) }
    val mAllGoodsAdapter by lazy { AllGoodsAdapter(this) }
    val mPresenter by lazy { GoodsPresenterImpl(this) }
    private val mRecyclerView by lazy { goods_rv }
    val mStickyListView: StickyListHeadersListView by lazy { goods_slhlv }

    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_goods, null)
    }

    override fun initData() {
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mGoodsTypeAdapter
        mStickyListView.adapter = mAllGoodsAdapter
    }

    override fun onError(msg: String) {
        toast(msg)
    }

    override fun initListener() {
        mPresenter.loadGoodsName("1")
    }

    override fun onSuccess(goodsTypes: List<GoodsTypeInfo>?, allGoods: List<GoodsInfo>?) {
        mGoodsTypeAdapter.updateGoodsTypes(goodsTypes)
        mAllGoodsAdapter.updateAllGoods(allGoods)

        mStickyListView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItem: Int, totleItemCount: Int) {
                val oldPosition = mGoodsTypeAdapter.mSelectPosition
                val newTypeId = mPresenter.allGoods[firstVisibleItem].typeId
                val newPosition = mPresenter.getTypePositionByTypeId(newTypeId)
                if (newPosition != oldPosition) {
                    mGoodsTypeAdapter.mSelectPosition = newPosition
                    mGoodsTypeAdapter.notifyDataSetChanged()
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            }
        })
    }
}