package com.mt.takeout.presenter.interf

import com.mt.takeout.model.bean.GoodsInfo

interface GoodsPresenter {
    fun loadGoodsName(sellerId: String)
    fun getGoodsPositionByTypeId(id: Int): Int
    fun getTypePositionByTypeId(typeId: Int): Int
    fun getCartList(): ArrayList<GoodsInfo>
    fun clearCart()
}