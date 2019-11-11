package com.lxj.sendmail

import android.app.Application

/**
 * @description sendMail
 * @author lixianjin
 * create on 2019-11-11 17:17
 */
class MyApplication : Application() {

    companion object {
        private var mFileName: String = "暂无"
        fun setFileName(fileName: String) {
            mFileName = fileName
        }

        fun getFileName() = mFileName
    }

    override fun onCreate() {
        super.onCreate()
    }
}