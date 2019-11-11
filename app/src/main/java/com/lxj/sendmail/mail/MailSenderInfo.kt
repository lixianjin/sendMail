package com.example.lixianjin.imageanimation

import java.util.*


/**
 * @description test
 * @author lixianjin
 * create on 2019-10-23 14:20
 */
data class MailSenderInfo(val mailServerHost: String, // 发送邮件的服务器的IP
                          val mailServerPort: String = "25", // 发送邮件的服务器的端口
                          val fromAddress: String, // 邮件发送者的地址
                          val toAddress: String, // 邮件接收者的地址
                          val bccAddress: String, // 抄送人地址
                          val ccAddress: String, // 密送人地址
                          val userName: String, // 登陆邮件发送服务器的用户名
                          val password: String, // 登陆邮件发送服务器的授权密码(不是登录密码，是SMTP授权码)
                          val validate: Boolean = false, // 是否需要身份验证
                          val subject: String, // 邮件主题
                          val content: String, // 邮件的文本内容
                          val attachFilePath: Array<String> // 邮件附件的路径
) {
    fun getProperties(): Properties {
        val p = Properties()
        p["mail.smtp.host"] = this.mailServerHost
        p["mail.smtp.port"] = this.mailServerPort
        p["mail.smtp.auth"] = if (validate) "true" else "false"
        return p
    }
}