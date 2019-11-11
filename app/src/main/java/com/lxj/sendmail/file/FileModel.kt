package com.lxj.sendmail.file

import java.io.Serializable

/**
 * @description sendMail
 * @author lixianjin
 * create on 2019-11-11 16:25
 */
data class FileModel(val isDir: Boolean, val name: String) : Serializable