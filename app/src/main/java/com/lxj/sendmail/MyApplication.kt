package com.lxj.sendmail

import android.app.Application
import java.text.SimpleDateFormat
import java.util.*

/**
 * @description sendMail
 * @author lixianjin
 * create on 2019-11-11 17:17
 */
class MyApplication : Application() {

    companion object {
        private var mFileName = "/storage/emulated/0/Netease/严选旺铺收银/log/app.log"
        val mPosCrash = "/storage/emulated/0/log/debug/er.plutus.debug-${longToDate(System.currentTimeMillis())}.dbg"
        fun setFileName(fileName: String) {
            mFileName = fileName
        }

        fun getFileName() = mFileName

        /**
         * 日期转换
         * @param lo
         * @return
         */
        private fun longToDate(lo: Long): String {
            val date = Date(lo)
            val sd = SimpleDateFormat("yyyyMMdd")
            return sd.format(date)
        }
    }
}