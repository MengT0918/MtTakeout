package com.mt.takeout.presenter.impl

import android.util.Log
import com.google.gson.Gson
import com.j256.ormlite.android.AndroidDatabaseConnection
import com.j256.ormlite.dao.Dao
import com.mt.takeout.base.BaseNetPresenterImpl
import com.mt.takeout.model.bean.User
import com.mt.takeout.model.dao.UserOpenHelper
import com.mt.takeout.presenter.interf.LoginPresenter
import com.mt.takeout.ui.activity.LoginActivity
import com.mt.takeout.utils.TakeoutApplication
import com.mt.takeout.utils.ThreadUtil
import java.sql.Savepoint

class LoginPresenterImpl(private val loginActivity: LoginActivity) : LoginPresenter, BaseNetPresenterImpl() {
    lateinit var connection :AndroidDatabaseConnection
    lateinit var savePoint : Savepoint

    override fun loginByPhone(phone: String) {
        val loginCall = mTakeoutService.loginByPhone(phone)
        loginCall.enqueue(callback)
    }

    override fun parserJson(json: String) {
        val user = Gson().fromJson(json, User::class.java)
        if (user != null) {
            //缓存到内存中，登录界面结束后，可以快速显示用户信息
            TakeoutApplication.sUser = user
            //还要缓存到数据库之中，这样应用退出后下次进入时能保存用户登录状态
            //数据库使用SqLiteOpenHelper，但是要自己写数据库语句，可以使用三方框架
            //常用ORM（对象关系映射）三方框架如：OrmLite、GreenDao
            //ORM：对象-关系映射，实现JavaBean的属性到数据库表的字段的映射
            val userOpenHelper = UserOpenHelper(loginActivity)
            val userDao: Dao<User, Int> = userOpenHelper.getDao(User::class.java)
            //创建新用户，直接create(userDao)
            //创建新用户 或者 更新老用户， 直接createOrUpdate(userDao)
            //区分新老用户， 并且统计比率（使用事务，保证数据库操作的完整性）
            try {
                connection  = AndroidDatabaseConnection(userOpenHelper.writableDatabase, true)
                savePoint = connection.setSavePoint("start")
                connection.isAutoCommit = false //取消自动提交
                val users = userDao.queryForAll()
                var isOldUser = users.any { user.id == it.id }
                if (isOldUser) {
                    userDao.update(user)
                    Log.i("LoginPresenterImpl","更新老用户信息")
                } else {
                    userDao.create(user)
                    Log.i("LoginPresenterImpl","创建新用户信息")
                }
                connection.commit(savePoint)
                ThreadUtil.runOnMainThread(Runnable { loginActivity.onSuccess(null, null) })
            } catch (e: Exception) {
                Log.e("LoginPresenterImpl","出现OrmLite事务异常," + e.localizedMessage)
                connection.rollback(savePoint) //事务回滚
                ThreadUtil.runOnMainThread(Runnable { loginActivity.onError("登录失败") })
            }
        } else {
            ThreadUtil.runOnMainThread(Runnable { loginActivity.onError("登录失败") })
        }
    }

    override fun responseFail() {
        ThreadUtil.runOnMainThread(Runnable { loginActivity.onError("服务器连接失败") })
    }
}