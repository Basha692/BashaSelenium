package util;

// javax.mail API is required to work with this code
// send email in Java from Gmail account with attachments

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailWithAttachment {

	public static void main(String args[]) throws Exception {
		Zip.zipDir(System.getProperty("user.dir") + "\\XSLT_Reports", System.getProperty("user.dir")
				+ "\\email_xlst_reports.zip");

		String host = "smtp.gmail.com";// host name
		String from = "amneetsingh72@gmail.com";// sender id
		String[] to = {"amneetsinghasr@gmail.com", "amneetsingh100@gmail.com"};// reciever id
		String pass = "Iliveinasr123";// sender's password
		String fileAttachment = "testReports/test_report.html";// file name for attachment
		// system properties
		Properties prop = System.getProperties();
		// Setup mail server properties
		prop.put("mail.smtp.gmail", host);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.user", from);
		prop.put("mail.smtp.password", pass);
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		// session
		Session session = Session.getInstance(prop, null);
		// Define message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));

		for (int i = 0; i < to.length; i++) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
		}

		message.setSubject("TEST AUTOMATION REPORT");
		// create the message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		// message body
		messageBodyPart
				.setText("Hi,\n\nPFA,the test automation report for the current iteration.\n\nThanks,\nAmneet Singh");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		// attachment
		messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(fileAttachment);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(fileAttachment);
		multipart.addBodyPart(messageBodyPart);
		message.setContent(multipart);
		// send message to reciever
		Transport transport = session.getTransport("smtp");
		transport.connect(host, from, pass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
}