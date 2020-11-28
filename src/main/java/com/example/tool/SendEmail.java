package com.example.tool;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail extends Thread { //经测试发送成功

    //用于给用户发送邮件的邮箱
    private final String from = "2630966248@qq.com";
    //邮箱的用户名
    private final String username = "2630966248@qq.com";
    //邮箱的密码 ostcvkxtsdyxeaih dkrwxryusxzaecii
    private final String password = "dkrwxryusxzaecii";
    //发送邮件的服务器地址
    private final String host = "smtp.qq.com";
    //邮件标题
    private String title;
    //邮件正文内容
    private String info;
    //收件人
    private String to;

    public SendEmail(String to, String title, String info) {
        this.to = to;
        this.title = title;
        this.info = info;
    }

    //重写run方法的实现，在run方法中发送邮件给指定的用户
    @Override
    public void run() {
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", host);
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");

            // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            //1、创建定义整个应用程序所需的环境信息的 Session 对象
            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    //发件人邮件用户名、授权码
                    return new PasswordAuthentication(username, password);
                }
            });

            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(true);

            //2、通过session得到transport对象
            Transport ts = session.getTransport();

            //3、使用邮箱的用户名和授权码连上邮件服务器
            ts.connect(host, username, password);

            //4、创建邮件
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from)); //发件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to)); //收件人
            message.setSubject(title);     //邮件的标题
            message.setContent(info, "text/html;charset=UTF-8");
            message.saveChanges();

            //5.发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
