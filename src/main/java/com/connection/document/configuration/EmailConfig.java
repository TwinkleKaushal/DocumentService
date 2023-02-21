package com.connection.document.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.connection.document.model.common.RequestModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailConfig {
	
	@Autowired
	private JavaMailSender javamailsender;
	
	/**
	 * This is method is used to send email with attached PDF
	 * @param to This is the destination email address
	 * @param subject  This is the subject of the email body
	 * @param emailBody This contains the password of the PDF
	 * @param resquest  This is the object of Request Model
	 * @throws MessagingException It is going to throw the custom message failure exception
	 * @throws IOException
	 */

		public void sendMail(String to, String subject, String emailBody,RequestModel resquest) throws MessagingException, IOException{
	        final String username = "kaushaltwinki@gmail.com";
	        final String password = "zxcowgqbvcsitoqf";
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        Session session = Session.getInstance(props,
	                new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        });

	        try {

	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress("kaushaltwinki@gmail.com"));
	            message.setRecipients(Message.RecipientType.TO,
	                    InternetAddress.parse(to));
	            message.setSubject(subject);
	            
	            
	          // create MimeBodyPart object and set your message text        
	          BodyPart messageBodyPart1 = new MimeBodyPart();     
	          messageBodyPart1.setText(" Thank you! For using our loan originating system"
	        		  +"You can check your details in this pdf. Your PDF password is:"+resquest.getPassword());          

	          // create new MimeBodyPart object and set DataHandler object to this object        
	          MimeBodyPart messageBodyPart2 = new MimeBodyPart();      
	          messageBodyPart2.attachFile(new File(emailBody), "application/pdf", null);
	         
	          // create Multipart object and add MimeBodyPart objects to this object        
	          Multipart multipart = new MimeMultipart();    
	          multipart.addBodyPart(messageBodyPart1);     
	          multipart.addBodyPart(messageBodyPart2);      

	            
	          message.setContent(multipart );        

	          //7) send message    
	          Transport.send(message);      
	          log.info("message sent...."); 
	            
	        } catch (MessagingException e) {
	            throw new RuntimeException(e);
	        }
	    }

}
