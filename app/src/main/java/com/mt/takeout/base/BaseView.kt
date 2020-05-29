package com.mt.takeout.base

interface BaseView<ITEMBEAN1, ITEMBEAN2> {
    fun onError(msg: String)
    fun onSuccess(nearbyList: List<ITEMBEAN1>?, otherList: List<ITEMBEAN2>?)
}