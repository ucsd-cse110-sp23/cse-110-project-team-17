package project.handler;

import java.util.*;
import javax.mail.*;
import javax.mail.Session;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

public class sendEmailHandler {

	String username;

	// SendEmailHandler constructor, initializes username field
	public sendEmailHandler() {
		this.username = "";
	}

	// Main method to send email given required fields
    public String sendEmailUtil (Session session, String toEmail, String fromEmail, String name, String subject, String body) {
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

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);  

	      return "Email successfully sent.\n";
	    }
	    catch (Exception e) {
	      return e.getMessage();
	    }
	}
    
	// Helper method that grabs the necessary info to send the email with
    public String sendEmailHelper(String toAddress, String subject, String body) {
        Map<String,String> dbMap = DBCreate.readEmailInformation(username);

		// For testing, use orpheus157@gmail.com and password vsuwlvowusinisbt
        final String fromEmail = dbMap.get("emailAddress_id");
		final String password = dbMap.get("emailPassword_id");
		final String toEmail = toAddress; // can be any email id 
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
		
		return this.sendEmailUtil(session, toEmail, fromEmail, name, subject, body);
		

    }

	// Method to set username
	public void setUsername(String username) {
		this.username = username;
	}
}
