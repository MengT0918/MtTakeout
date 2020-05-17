package com.mt.takeout.presenter.interf

interface LoginPresenter {
    /**
     * 使用手机号码登录
     */
    fun loginByPhone(phone: String)
}