package com.mt.takeout.ui.activity

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.util.Log
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.mt.takeout.R
import com.mt.takeout.base.BaseActivity
import com.mt.takeout.model.bean.User
import com.mt.takeout.presenter.impl.LoginPresenterImpl
import com.mt.takeout.utils.SMSUtil
import com.mt.takeout.utils.ThreadUtil
import com.mt.takeout.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.lang.Exception

class LoginActivity : BaseActivity(), LoginView {
    private val mPresenter by lazy { LoginPresenterImpl(this) }
    private var mTime = 60
    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            msg?.let {
                when (it.what) {
                    TIME_MINUS -> login_user_code_tv.text = "剩余${mTime}秒"
                    TIME_OUT -> {
                        login_user_code_tv.isEnabled = true
                        login_user_code_tv.setBackgroundColor(
                            this@LoginActivity.resources.getColor(
                                R.color.colorPrimary
                            )
                        )
                        login_user_code_tv.text = "点击重发"
                        mTime = 60
                    }
                }
            }
        }
    }
    private val mEventHandler = object : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any) {
            if (data is Throwable) {
                Log.e("LoginActivity", "EventHandler Throwable " + data.message)
                ThreadUtil.runOnMainThread(Runnable { toast("登陆失败") })
            } else {
                when (event) {
                    SMSSDK.EVENT_GET_VERIFICATION_CODE ->
                        Log.e("LoginActivity", "EventHandler 获取验证码成功 ")
                    SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> {
                        Log.e("LoginActivity", "EventHandler 提交验证码成功 ")
                        val phone = login_user_phone.text.toString().trim()
                        mPresenter.loginByPhone(phone)
                    }
                }
            }
        }
    }

    companion object {
        const val TIME_MINUS = -1
        const val TIME_OUT = 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initListener() {
        //注册EventHandler
        SMSSDK.registerEventHandler(mEventHandler)
        login_user_back.onClick { finish() }
        login_user_code_tv.onClick {
            val phone = login_user_phone.text.toString().trim()
            if (SMSUtil.judgePhoneNums(this@LoginActivity, phone)) {
                SMSSDK.getVerificationCode("86", phone)
                login_user_code_tv.isEnabled = false
                login_user_code_tv.setBackgroundColor(this@LoginActivity.resources.getColor(R.color.gray))
                //获取验证码间隔60秒
                Thread(Runnable {
                    while (mTime > 0) {
                        mHandler.sendEmptyMessage(TIME_MINUS)
                        SystemClock.sleep(999)
                        mTime--
                    }
                    mHandler.sendEmptyMessage(TIME_OUT)
                }).start()
            }
        }
        login_verify.onClick {
            val phone = login_user_phone.text.toString().trim()
            val code = login_user_code_et.text.toString().trim()
            if (SMSUtil.judgePhoneNums(this@LoginActivity, phone) && code.isNotEmpty()) {
                SMSSDK.submitVerificationCode("86", phone, code)
            } else {
                toast("登陆失败")
            }
        }
    }

    // 使用完EventHandler需注销，否则可能出现内存泄漏
    override fun onDestroy() {
        super.onDestroy()
        SMSSDK.unregisterEventHandler(mEventHandler)
    }

    override fun onError(msg: String) {
        toast(msg)
    }

    override fun onSuccess(nearbyList: List<User>?, otherList: List<User>?) {
        toast("登陆成功")
        finish()
    }
}