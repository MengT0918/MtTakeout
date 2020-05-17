package com.mt.takeout.model.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.mt.takeout.model.bean.User

class UserOpenHelper(private val context: Context) :
    OrmLiteSqliteOpenHelper(context, "user_kotlin.db", null, 1) {
    /**
     * 第一次创建数据库调用
     */
    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTable(connectionSource, User::class.java)
    }

    /**
     * 跟新数据库
     */
    override fun onUpgrade(
        database: SQLiteDatabase?,
        connectionSource: ConnectionSource?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }
}