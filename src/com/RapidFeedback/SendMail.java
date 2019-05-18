package com.RapidFeedback;
import java.util.*;  
import javax.activation.*;  
import javax.mail.*;  
import javax.mail.internet.*;  

public class SendMail {
	private String host = "";  //smtp server  
	private String from = "";  //send address  
	private String to = "";    //receiver address  
	private String affix = ""; //attachment local address  
	private String affixName = ""; //attachment name in mail  
	private String user = "";  //mail account  
	private String pwd = "";   //mail password  
	private String subject = ""; //mail subject  
	   
	public void setAddress(String from,String to,String subject){  
		this.from = from;  
		this.to   = to;  
		this.subject = subject;  
	}  
		   
	public void setAffix(String affix,String affixName){  
		this.affix = affix;  
		this.affixName = affixName;  
	}  
		   
	public void send(String host,String user,String pwd) {  
		this.host = host;  
		this.user = user;  
		this.pwd  = pwd;  
		  
		Properties props = new Properties();  
			
		//set the mail smpt host）  
		props.put("mail.smtp.host", host);  
		//the authority
		props.put("mail.smtp.auth", "true");  
		//create a session
		Session session = Session.getDefaultInstance(props);  
		 
		//to show the send process in console 
		session.setDebug(true);  
			
		//the message  
		MimeMessage message = new MimeMessage(session);  
		try{  
			//load the sender's address  
		    message.setFrom(new InternetAddress(from));  
		    //load the receiver address
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
		    //load the subject  
		    message.setSubject(subject);  
		    
		    // add the text and the attachment to the multipart  
		    Multipart multipart = new MimeMultipart();           
		    
		    
		    //set the body part
		    BodyPart contentPart = new MimeBodyPart();  
		
		    contentPart.setText("This is a feedback for your COMP9000 Asignment1 Presentation.\r\n"
		    		+ "If you have any problems, please dont hesitate to contact the lecturers/tutors.");  
		    multipart.addBodyPart(contentPart);  
		    //load the attachment from the local machine 
		    BodyPart messageBodyPart= new MimeBodyPart();  
		    DataSource source = new FileDataSource(affix);  
		    //add the content of the attachment
		    messageBodyPart.setDataHandler(new DataHandler(source));  
		    //add the title of the attachment, ONLY ENGLISH!!
		    messageBodyPart.setFileName(affixName);  
		    multipart.addBodyPart(messageBodyPart);  
		    
		    
		    //put the multipat into the mail content 
		    message.setContent(multipart);  
		    //save the message 
		    message.saveChanges();  
		    // send  
		    Transport transport = session.getTransport("smtp");  
		    //connect to the host mail box
		    transport.connect(host, user, pwd);  
		    //send the mail 
		    transport.sendMessage(message, message.getAllRecipients());  
		    transport.close();  
		}catch(Exception e){  
		    e.printStackTrace();  
		}  
	}    
}
