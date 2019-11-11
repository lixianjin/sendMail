package com.lxj.sendmail.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author lixianjin
 * create on 2019-10-23 14:51
 * @description test
 */
public class MyAuthenticator extends Authenticator {

    String userName=null;
    String password=null;

    public MyAuthenticator(){
    }
    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName, password);
    }
}
