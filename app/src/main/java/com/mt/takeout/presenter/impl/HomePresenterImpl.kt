package com.mt.takeout.presenter.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mt.takeout.base.BasePresenterImpl
import com.mt.takeout.model.bean.HomeSeller
import com.mt.takeout.presenter.interf.HomePresenter
import com.mt.takeout.utils.ThreadUtil
import com.mt.takeout.view.HomeView
import org.json.JSONObject

class HomePresenterImpl(var homeView: HomeView) : HomePresenter, BasePresenterImpl() {
    override fun loadData() {
        val homeCall = mHomeService.getHomeInfo()
        homeCall.enqueue(callback)
    }

    override fun parserJson(json: String) {
        val jsonObject = JSONObject(json)
        val nearby = jsonObject.getString("nearbySellerList")
        val other = jsonObject.getString("otherSellerList")
        val gson = Gson()
        val nearbyList = gson.fromJson<List<HomeSeller>>(nearby, object : TypeToken<List<HomeSeller>>() {}.type)
        val otherList = gson.fromJson<List<HomeSeller>>(other, object : TypeToken<List<HomeSeller>>() {}.type)
        if (nearbyList.isNotEmpty() || otherList.isNotEmpty()) {
            ThreadUtil.runOnMainThread(Runnable { homeView.onSuccess(nearbyList, otherList) })
        } else {
            ThreadUtil.runOnMainThread(Runnable { homeView.onError("获取首页数据失败") })
        }
    }

    override fun responseFail() {
        ThreadUtil.runOnMainThread(Runnable { homeView.onError("服务器连接失败") })
    }
}