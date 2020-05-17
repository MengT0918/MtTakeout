package com.mt.takeout.base

interface BaseView<ITEMBEAN> {
    fun onError(msg: String)
    fun onSuccess(nearbyList: List<ITEMBEAN>?, otherList: List<ITEMBEAN>?)
}