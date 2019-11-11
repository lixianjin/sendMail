package com.lxj.sendmail.mail

import com.example.lixianjin.imageanimation.MailSenderInfo
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


/**
 * @description test
 * @author lixianjin
 * create on 2019-10-23 14:25
 */
class SimpleMailSender {
    /**
     * 以文本格式发送邮件
     * @param mailInfo 待发送的邮件的信息
     */
    fun sendTextMail(mailInfo: MailSenderInfo): Boolean {
        // 判断是否需要身份认证
        var authenticator: MyAuthenticator? = null
        val pro = mailInfo.getProperties()
        if (mailInfo.validate) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = MyAuthenticator(mailInfo.userName, mailInfo.password)
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        val sendMailSession = Session.getDefaultInstance(pro, authenticator)
        try {
            // 根据session创建一个邮件消息
            val mailMessage = MimeMessage(sendMailSession)
            // 创建邮件发送者地址
            // 设置邮件消息的发送者
            mailMessage.setFrom(InternetAddress(mailInfo.fromAddress))
            // 创建邮件的接收者地址，并设置到邮件消息中
            mailMessage.setRecipient(Message.RecipientType.TO, InternetAddress(mailInfo.toAddress))
            // 抄送
            mailMessage.setRecipients(Message.RecipientType.BCC, arrayOf(InternetAddress(mailInfo.bccAddress)))
            // 密送
//            mailMessage.setRecipient(Message.RecipientType.CC, InternetAddress(mailInfo.ccAddress))
            // 设置邮件消息的主题
            mailMessage.subject = mailInfo.subject
            // 设置邮件消息发送的时间
            mailMessage.sentDate = Date()
            // 设置邮件消息的主要内容
            mailMessage.setText(mailInfo.content)
            // 附件
            val allMultipart = MimeMultipart("mixed") //附件
            mailInfo.attachFilePath.forEach {
                val fds = FileDataSource(it) //打开要发送的文件
                val attachPart = MimeBodyPart().apply {
                    dataHandler =  DataHandler(fds)
                    fileName = fds.name
                }

                allMultipart.addBodyPart(attachPart)//添加
            }
            // setText设置内容在添加附件后会出现内容没有的情况，解决方案如下
            // start
            val content = MimeBodyPart()
            val bodyMultipart = MimeMultipart("related")
            val html = MimeBodyPart()
            html.setContent(mailInfo.content, "text/html;charset=utf-8")
            bodyMultipart.addBodyPart(html)
            content.setContent(bodyMultipart)
            allMultipart.addBodyPart(content)
            // end

            mailMessage.setContent(allMultipart)
            mailMessage.saveChanges()
            // 发送邮件
            Transport.send(mailMessage)
            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return false
    }

    /**
     * 以HTML格式发送邮件
     * @param mailInfo 待发送的邮件信息
     */
    fun sendHtmlMail(mailInfo: MailSenderInfo): Boolean {
        // 判断是否需要身份认证
        var authenticator: MyAuthenticator? = null
        val pro = mailInfo.getProperties()
        //如果需要身份认证，则创建一个密码验证器
        if (mailInfo.validate) {
            authenticator = MyAuthenticator(mailInfo.userName, mailInfo.password)
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        val sendMailSession = Session.getDefaultInstance(pro, authenticator)
        try {
            // 根据session创建一个邮件消息
            val mailMessage = MimeMessage(sendMailSession)
            // 创建邮件发送者地址
            // 设置邮件消息的发送者
            mailMessage.setFrom(InternetAddress(mailInfo.fromAddress))
            // 创建邮件的接收者地址，并设置到邮件消息中
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO, InternetAddress(mailInfo.toAddress))
            // 设置邮件消息的主题
            mailMessage.subject = mailInfo.subject
            // 设置邮件消息发送的时间
            mailMessage.sentDate = Date()
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            val mainPart = MimeMultipart()
            // 创建一个包含HTML内容的MimeBodyPart
            val html = MimeBodyPart()
            // 设置HTML内容
            html.setContent(mailInfo.content, "text/html; charset=utf-8")
            mainPart.addBodyPart(html)
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart)
            // 附件
            val allMultipart = MimeMultipart("mixed") //附件
            mailInfo.attachFilePath.forEach {
                val fds = FileDataSource(it) //打开要发送的文件
                val attachPart = MimeBodyPart().apply {
                    dataHandler =  DataHandler(fds)
                    fileName = fds.name
                }

                allMultipart.addBodyPart(attachPart)//添加
            }
            mailMessage.setContent(allMultipart)
            mailMessage.saveChanges()
            // 发送邮件
            Transport.send(mailMessage)
            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return false
    }
}