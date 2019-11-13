package com.lxj.sendmail

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.lixianjin.imageanimation.MailSenderInfo
import com.lxj.sendmail.file.FileActivity
import com.lxj.sendmail.mail.SimpleMailSender
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.onUiThread
import org.jetbrains.anko.toast

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
        btn_choose_other_log.setOnClickListener {
            FileActivity.openFileActivity(this, getSdCardPath())
        }

        // 发送点击
        btn_send_log.setOnClickListener {
            async {
                if (sendMail(false)) {
                    onUiThread {
                        toast("发送成功！")
                        showSendSuccessDialog()
                    }
                }
            }
        }

        btn_send_log_with_pos.setOnClickListener {
            async {
                if (sendMail(true)) {
                    onUiThread {
                        toast("发送成功！")
                    }
                }
            }
        }
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
    private fun sendMail(isSendPos: Boolean): Boolean {
        val list = if (isSendPos) arrayOf(MyApplication.getFileName()) else arrayOf(MyApplication.getFileName(), MyApplication.mPosCrash)

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
            attachFilePath = list
        )
        val sms = SimpleMailSender()
        return sms.sendTextMail(mailInfo)
    }

    private fun showSendSuccessDialog(){
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_dialog, null, false))
        dialog.show()

        async {
            Thread.sleep(3000)
            onUiThread {
                dialog.dismiss()
            }
        }

    }
}
