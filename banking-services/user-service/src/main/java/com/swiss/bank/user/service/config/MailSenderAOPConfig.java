package com.swiss.bank.user.service.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.services.impl.MailSenderService;

import jakarta.mail.MessagingException;

@Service
@Aspect
public class MailSenderAOPConfig {
	
	MailSenderService mailSenderService;
	
	public MailSenderAOPConfig(MailSenderService mailSenderService) {
		this.mailSenderService = mailSenderService;
	}
	
	@Before("@annotation(com.swiss.bank.user.service.annotation.MailAlert)")
    public void beforeMethodExecution(JoinPoint joinPoint) {
		sendMail();
    }

    @After("@annotation(com.swiss.bank.user.service.annotation.MailAlert)")
    public void afterMethodExecution(JoinPoint joinPoint){
		sendMail();
    }
    

    @AfterThrowing("@annotation(com.swiss.bank.user.service.annotation.MailAlert)")
    public void afterErrorInMethodExecution(JoinPoint joinPoint){
		sendMail();
    }
    
    private void sendMail() {
    	try {
			mailSenderService.sendMail(null, null, null, null);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    }
}
