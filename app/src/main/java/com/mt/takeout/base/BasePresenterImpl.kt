package com.mt.takeout.base

import com.mt.takeout.net.HomeService
import com.mt.takeout.net.ResponseInfo
import com.mt.takeout.utils.ThreadUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BasePresenterImpl() {
    private val HOST = "http://192.168.0.100:8080"
    protected val mHomeService: HomeService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("$HOST/TakeoutService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mHomeService = retrofit.create<HomeService>(HomeService::class.java)
    }

    protected val callback = object : Callback<ResponseInfo> {
        override fun onFailure(call: Call<ResponseInfo>?, t: Throwable?) {
            responseFail()
        }

        override fun onResponse(call: Call<ResponseInfo>?, response: Response<ResponseInfo>?) {
            response?.let {
                if (it.isSuccessful) {
                    val responseInfo = it.body()
                    if ("0" == responseInfo.code) {
                        val json = responseInfo.data
                        parserJson(json)
                    }
                }
            }
        }
    }

    /**
     * 解析JSON字符串
     */
    abstract fun parserJson(json: String)

    /**
     * 网络请求失败
     */
    abstract fun responseFail()
}