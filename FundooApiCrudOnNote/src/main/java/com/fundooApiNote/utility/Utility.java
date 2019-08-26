package com.fundooApiNote.utility;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class Utility
{
	@Autowired
	private static JavaMailSender javaMailSender;
	
	private static String token;
	
	public static String getToken() 
	{
		return token;
	}

	public static void setToken(String token) 
	{
		Utility.token = token;
	}
	
	public Utility(JavaMailSender javaMailSender) 
	{
        Utility.javaMailSender = javaMailSender;
    }

	public static String sendToken(String emailid, String subject, String token1)
	{
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);


		try 
		{
			helper.setFrom("bhagyashrichalke21@gmail.com");
		    helper.setTo(emailid);
		    helper.setText(token1);
		    helper.setSubject(subject);
		}
		catch(MessagingException e) 
		{
		    e.printStackTrace();
		    return "Error while sending mail...";
		}
		javaMailSender.send(message);
		return "Mail Sent Successfully!";
		  
	}
	
}