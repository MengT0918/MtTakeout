package com.mt.takeout.utils

import com.mob.MobApplication
import com.mt.takeout.model.bean.User

class TakeoutApplication : MobApplication() {
    companion object {
        var sUser = User()
    }

    /**
     * 应用程序入口
     */
    override fun onCreate() {
        super.onCreate()

        sUser.id = -1 //未登录的用户id = -1
    }
}