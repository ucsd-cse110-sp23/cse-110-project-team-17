package project.handler;

import java.util.*;
import project.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;

public class sendEmailHandler {
    public void sendEmailUtil (Session session, String toEmail, String fromEmail, String name, String subject, String body) {
        try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress(fromEmail, name));

	      msg.setReplyTo(InternetAddress.parse(fromEmail, false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);  

	      System.out.println("EMail Sent Successfully!!");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
    
    public sendEmailHandler(String username, String subject, String body) {
        Map<String,String> dbMap = DBCreate.readEmailInformation(username);

        final String fromEmail = dbMap.get("emailAddress_id");
		final String password = dbMap.get("emailPassword_id");
		final String toEmail = "fill w/ params"; // can be any email id 
        final String name = dbMap.get("displayName_id");
		
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", dbMap.get("SMTPHost_id")); //SMTP Host
		props.put("mail.smtp.port", dbMap.get("TLSPort_id")); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		
		this.sendEmailUtil(session, toEmail, fromEmail, name, subject, body);
		

    }
}
