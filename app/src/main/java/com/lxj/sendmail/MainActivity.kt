package com.lxj.sendmail

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.lixianjin.imageanimation.MailSenderInfo
import com.lxj.sendmail.file.FileActivity
import com.lxj.sendmail.mail.SimpleMailSender
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.onUiThread
import org.jetbrains.anko.toast
import java.io.*

class MainActivity : AppCompatActivity() {

    companion object {
        //读写权限
        private val PERMISSIONS_STORAGE = arrayOf(
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )
        //请求状态码
        private const val REQUEST_PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 申请文件读写权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_PERMISSION_CODE
                );
            }
        }

        // 选择文件
        btn_select_file.setOnClickListener {
            FileActivity.openFileActivity(this, getSdCardPath())
        }

        // 发送点击
        btn_send_mail.setOnClickListener {
            async {
                if (sendMail()) {
                    onUiThread {
                        toast("发送成功！")
                    }
                }
            }
        }
    }

    override fun onResume() {
        tv_file_path.text = MyApplication.getFileName()
        super.onResume()
    }

    /**
     * 权限申请结果回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (i in permissions.indices) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i])
            }
        }
    }


    private fun isSdCardExist(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    private fun getSdCardPath(): String {
        return if (isSdCardExist()) {
            Environment.getExternalStorageDirectory().absolutePath // /storage/emulated/0
        } else {
            "no sdcard"
        }
    }

    /**
     * 发送邮件
     */
    private fun sendMail(): Boolean {
        val mailInfo = MailSenderInfo(
            mailServerHost = "smtp.126.com",
            mailServerPort = "25",  // 465(不可以) 587 25
            fromAddress = "zhouqianrutest@126.com",
            toAddress = "wb.lixianjin@mesg.corp.netease.com",
            bccAddress = "wb.lixianjin@mesg.corp.netease.com", // zhouqianru@corp.netease.com
            ccAddress = "",
            userName = "zhouqianrutest@126.com",
            password = "test123456",
            validate = true,
            subject = "人工收银日志",
            content = "人工收银最新log日志和crash日志",
            attachFilePath = arrayOf(MyApplication.getFileName())
        )
        val sms = SimpleMailSender()
        return sms.sendTextMail(mailInfo)
    }

    /**
     * 读取SD卡文件里面的内容
     */
    fun readFile() {
        val file = File("/mnt/sdcard/Netease/严选旺铺收银/log/app.log")

        // 1.0 start
        val fr = FileReader("/mnt/sdcard/Netease/严选旺铺收银/log/app.log")
        val r = BufferedReader(fr)
        while (r.readLine() != null) {
            Log.i("LXJ", "1.0-SD卡文件里面的内容:${r.readLine()}")
        }
        // end

        // 2.0 start
        try {
            val iss = FileInputStream(file)
            val input = InputStreamReader(iss, "UTF-8")
            val reader = BufferedReader(input)
            while (reader.readLine() != null) {
                Log.i("LXJ", "2.0-SD卡文件里面的内容:${reader.readLine()}")
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        // end

        // 3.0 start
        try {
            FileReader(file).use { reader ->
                reader.readLines().forEach {
                    Log.i("LXJ", "3.0-SD卡文件里面的内容:$it")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // end
    }
}
