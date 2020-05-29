package com.mt.takeout.presenter.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mt.takeout.base.BaseNetPresenterImpl
import com.mt.takeout.model.bean.GoodsInfo
import com.mt.takeout.model.bean.GoodsTypeInfo
import com.mt.takeout.presenter.interf.GoodsPresenter
import com.mt.takeout.utils.ThreadUtil
import com.mt.takeout.view.GoodsView
import org.json.JSONObject
import java.util.ArrayList

class GoodsPresenterImpl(private val goodsView: GoodsView) : GoodsPresenter,
    BaseNetPresenterImpl() {
    val allGoods = arrayListOf<GoodsInfo>()
    var goodsTypes: List<GoodsTypeInfo> = arrayListOf<GoodsTypeInfo>()

    override fun loadGoodsName(sellerId: String) {
        val businessCall = mTakeoutService.getBusinessInfo(sellerId)
        businessCall.enqueue(callback)
    }

    override fun getGoodsPositionByTypeId(id: Int): Int {
        var index = -1;
        for (i in allGoods.indices) {
            if (allGoods[i].typeId == id) {
                index = i
                break
            }
        }
        return index
    }

    override fun getTypePositionByTypeId(typeId: Int): Int {
        var index = -1
        for (i in goodsTypes.indices) {
            if (goodsTypes[i].id == typeId) {
                index = i
                break
            }
        }
        return index
    }

    override fun getCartList(): ArrayList<GoodsInfo> {
        val carts = ArrayList<GoodsInfo>()
        for (i in allGoods.indices) {
            val goodsInfo = allGoods[i]
            if (goodsInfo.count > 0) {
                carts.add(goodsInfo)
            }
        }
        return carts
    }

    override fun clearCart() {
        getCartList().map { it.count = 0 }
    }

    override fun parserJson(json: String) {
        val jsonObject = JSONObject(json)
        val list = jsonObject.getString("list")
        goodsTypes = Gson().fromJson<List<GoodsTypeInfo>>(
            list,
            object : TypeToken<List<GoodsTypeInfo>>() {}.type
        )
        if (goodsTypes.isNotEmpty()) {
            goodsTypes.indices.forEach {
                val goodsType = goodsTypes[it]
                val goods = goodsType.list
                goods.indices.forEach { index ->
                    goods[index].typeId = goodsType.id
                    goods[index].typeName = goodsType.name
                }
                allGoods.addAll(goods)
            }
            ThreadUtil.runOnMainThread(Runnable { goodsView.onSuccess(goodsTypes, allGoods) })
        } else {
            ThreadUtil.runOnMainThread(Runnable { goodsView.onError("获取商品信息失败") })
        }
    }

    override fun responseFail() {
        ThreadUtil.runOnMainThread(Runnable { goodsView.onError("服务器连接失败") })
    }
}