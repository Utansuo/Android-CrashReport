package com.example.crashtest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;

import android.util.Log;



public class SendMaile {
	
 public	static SendMaile  sendMaile;
	static MailSenderInfo mailSenderInfo;
	public static SendMaile getInstance() {
		if(sendMaile ==null)
		{
			sendMaile = new SendMaile();
			mailSenderInfo = new MailSenderInfo();
		}
		return sendMaile;
	}
	
	public void send(String con)
	{
		mailSenderInfo.setContent(con);
		if( sendTextMail(mailSenderInfo))
		{
			Log.e("STATE", "Success");
		}
		else {
			
		}
	}
	
	 public boolean sendTextMail(MailSenderInfo mailInfo) 
	    {
	        // 判断是否需要身份认证    
	        MyAuthenticator authenticator = null;    
	        Properties pro = mailInfo.getProperties();   
	        if (mailInfo.isValidate()) 
	        {    
	            // 如果需要身份认证，则创建一个密码验证器    
	            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
	        }   
	        // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
	        Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
	        try 
	        {    
	            // 根据session创建一个邮件消息    
	            Message mailMessage = new MimeMessage(sendMailSession);    
	            // 创建邮件发送者地址    
	            Address from = new InternetAddress(mailInfo.getFromAddress());    
	            // 设置邮件消息的发送者    
	            mailMessage.setFrom(from);    
	            // 创建邮件的接收者地址，并设置到邮件消息中    
	            Address to = new InternetAddress(mailInfo.getToAddress());    
	            mailMessage.setRecipient(Message.RecipientType.TO,to);    
	            String[] ccs = mailInfo.getCcs();
	            
				if (ccs != null)
				{
				    // 为每个邮件接收者创建一个地址
				    Address[] ccAdresses = new InternetAddress[ccs.length];
				    for (int i=0; i<ccs.length; i++)
				    {
				        ccAdresses[i] = new InternetAddress(ccs[i]);
				    }
				    mailMessage.setRecipients(Message.RecipientType.CC, ccAdresses);
				}
	            // 设置邮件消息的主题    
	            mailMessage.setSubject(mailInfo.getSubject());    
	            // 设置邮件消息发送的时间    
	            mailMessage.setSentDate(new Date());    
	            // 设置邮件消息的主要内容    
	            String mailContent = mailInfo.getContent();    
	            mailMessage.setText(mailContent);    
	            // 发送邮件    
	            Transport.send(mailMessage);   
	            return true;    
	        } 
	        catch (MessagingException ex) 
	        {    
	            ex.printStackTrace();    
	        }    
	        return false;    
	    }   

}

 
 class MyAuthenticator extends Authenticator 
 {
 	    String userName;   
 	    String password;   
 	        
 	    public MyAuthenticator()
 	    {   
 	    }   
 	    
 	    public MyAuthenticator(String username, String password) 
 	    {
 	        this.userName = username;    
 	        this.password = password;    
 	    }    
 	    
 	    protected PasswordAuthentication getPasswordAuthentication()
 	    {   
 	        return new PasswordAuthentication(userName, password);   
 	    }  
 }
 
class MailSenderInfo 
 {
 	// ������������������IP������    
     private String mailServerHost="smtp.163.com";    
     private String mailServerPort = "25";   
     
     // ����������������    
     private String fromAddress = "daxiaojianghutest@163.com";    
     // ����������������    
      private String toAddress ="hongfei.wu@chukong-inc.com"; //"hongfei.wu@chukong-inc.com";    
     // ��������������������������������    
//     private String userName;    
//     private String password;    
     
     String userName="daxiaojianghutest@163.com";   
	    String password="wwy123456";   
     // ����������������    
     private boolean validate = true;    
     // ��������    
     private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");  
     private String subject ;    
     // ��������������    
     private String content;    
     // ����������������    
     private String[] attachFileNames;     
     private String[] ccs = new String[]{"wuyuan.wu@chukong-inc.com","wu838649825@qq.com"};
     /**   
       * ����������������   
       */    
     public Properties getProperties()
     {    
       Properties p = new Properties();    
       p.put("mail.smtp.host", this.mailServerHost);    
       p.put("mail.smtp.port", this.mailServerPort);    
       p.put("mail.smtp.auth", validate ? "true" : "false");    
       return p;    
     }    
     
     public String getMailServerHost() 
     {    
       return mailServerHost;    
     }    
     
     public void setMailServerHost(String mailServerHost) 
     {
       this.mailServerHost = mailServerHost;    
     }   
     
     public String getMailServerPort() 
     {    
       return mailServerPort;    
     }   
     
     public void setMailServerPort(String mailServerPort) 
     {    
       this.mailServerPort = mailServerPort;    
     }   
     
     public boolean isValidate() 
     {    
       return validate;    
     }   
     
     public void setValidate(boolean validate) 
     {    
       this.validate = validate;    
     }   
     
     public String[] getAttachFileNames() 
     {    
       return attachFileNames;    
     }   
     
     public void setAttachFileNames(String[] fileNames) 
     {    
       this.attachFileNames = fileNames;    
     }   
     
     public String getFromAddress() 
     {    
       return fromAddress;    
     }    
     
     public void setFromAddress(String fromAddress) 
     {    
       this.fromAddress = fromAddress;    
     }   
     
     public String getPassword() 
     {    
       return password;    
     }   
     
     public void setPassword(String password) 
     {    
       this.password = password;    
     }   
     
     public String getToAddress() 
     {    
       return toAddress;    
     }    
     
     public void setToAddress(String toAddress) 
     {    
       this.toAddress = toAddress;    
     }    
     
     public String getUserName() 
     {    
       return userName;    
     }   
     
     public void setUserName(String userName) 
     {    
       this.userName = userName;    
     }   
     
     public String getSubject() 
     {  
    	 subject ="DaXiao_Android_CrashReport"+ formatter.format(new Date());  
       return subject;    
     }   
     
     public void setSubject(String subject) 
     {    
       this.subject = subject;    
     }   
     
     public String getContent() 
     {    
       return content;    
     }   
     
     public void setContent(String textContent)
     {    
       this.content = textContent;    
     }   
     
     public String[] getCcs() {

    	      return ccs;
    }
     public void setCcs(String[] ccs) { 
    	     this.ccs = ccs;
    }

 }
