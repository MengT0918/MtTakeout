package com.mt.takeout.ui.fragment

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mt.takeout.R
import com.mt.takeout.adapter.HomeAdapter
import com.mt.takeout.base.BaseFragment
import com.mt.takeout.dagger2.component.DaggerHomeFragmentComponent
import com.mt.takeout.dagger2.module.HomeFragmentModule
import com.mt.takeout.model.bean.HomeSeller
import com.mt.takeout.presenter.impl.HomePresenterImpl
import com.mt.takeout.view.HomeView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_title.*
import org.jetbrains.anko.support.v4.toast
import java.util.ArrayList
import javax.inject.Inject

class HomeFragment : BaseFragment(), HomeView {
    private var mScrollSum = 0
    private val mAllList = ArrayList<HomeSeller>()
    private val mAdapter by lazy { HomeAdapter() }
    @Inject
    lateinit var mPresenter: HomePresenterImpl

    override fun initView(): View? {
        return View.inflate(activity, R.layout.fragment_home, null)
    }

    override fun initData() {
        home_rv.layoutManager = LinearLayoutManager(activity)
        home_rv.adapter = mAdapter

        //解耦View层和Presenter层，记载mPresenter对象
        DaggerHomeFragmentComponent.builder().homeFragmentModule(HomeFragmentModule(this)).build().inject(this)
        //加载数据
        mPresenter.loadData()
    }

    override fun initListener() {
        home_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val distance = item_home_title_slider.height - home_title_container.height
                mScrollSum += dy
                val alpha = if (mScrollSum >= distance) 255 else 55 + 200 * mScrollSum / distance
                home_title_container.setBackgroundColor(Color.argb(alpha, 0x31, 0x90, 0xE8))
            }
        })
    }

    override fun onError(msg: String) {
        toast(msg)
    }

    override fun onSuccess(nearbyList: List<HomeSeller>?, otherList: List<HomeSeller>?) {
        mAllList.clear()
        nearbyList?.let { mAllList.addAll(it) }
        otherList?.let { mAllList.addAll(it) }
        mAdapter.updateData(mAllList)
    }
}