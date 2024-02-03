package jfs.backend.user.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;
	
	public boolean sendEmail(String to,String subject,String body) {
		
		boolean isMailSent= false;
		
		try {
			
			MimeMessage mimeMessage = mailSender.createMimeMessage(); // to set mail to, subject, and body
			
			MimeMessageHelper helper= new MimeMessageHelper(mimeMessage); // to set value for arguments
			
			helper.setTo(to);
			helper.setFrom("krkhengate123@gmail.com");
			helper.setSubject(subject);
			
			// you can add same like attachment, cc, bcc
			
			helper.setText(body, true); // if you want to include html tag inside the mail you can use true
			
			mailSender.send(mimeMessage);
			 
			isMailSent = true;
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return isMailSent;
		
	}
	
}
