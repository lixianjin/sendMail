package com.lxj.sendmail

import android.app.Application

/**
 * @description sendMail
 * @author lixianjin
 * create on 2019-11-11 17:17
 */
class MyApplication : Application() {

    companion object {
        private var mFileName: String = "/storage/emulated/0/Netease/严选旺铺收银/log/app.log"
        fun setFileName(fileName: String) {
            mFileName = fileName
        }

        fun getFileName() = mFileName
    }
}