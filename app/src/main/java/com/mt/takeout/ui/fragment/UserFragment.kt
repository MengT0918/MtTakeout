package com.mt.takeout.ui.fragment

import android.view.View
import com.mt.takeout.R
import com.mt.takeout.base.BaseFragment
import com.mt.takeout.ui.activity.LoginActivity
import com.mt.takeout.TakeoutApplication
import kotlinx.android.synthetic.main.fragment_user.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class UserFragment : BaseFragment() {
    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_user, null)
    }

    override fun initListener() {
        user_login.onClick { startActivity<LoginActivity>() }
    }

    override fun onStart() {
        super.onStart()
        //使用缓存，判断用户是否登陆了
        val user = TakeoutApplication.sUser
        if(user.id != -1) {
            //已登陆
            user_name.text =  "欢迎您，${user.name}"
            user_phone.text = user.phone
            user_info.visibility = View.VISIBLE
            user_login.visibility = View.GONE
        } else {
            //未登录
            user_info.visibility = View.GONE
            user_login.visibility = View.VISIBLE
        }
    }
}