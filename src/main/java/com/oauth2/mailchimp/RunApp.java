package com.oauth2.mailchimp;


import com.oauth2.mailchimp.service.ConnectService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunApp {

    public static void main(String[] args) {
        try{
            ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
            ConnectService connService = (ConnectService)context.getBean("connectService");
            System.out.println("Connection to MailChimp API started...");
            connService.process();
        } catch (Throwable ex){
            ex.printStackTrace();
        }
    }
}
