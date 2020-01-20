package q.rest.vendor.operation;

import q.rest.vendor.helper.AppConstants;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Stateless
public class AsyncService {



    @Asynchronous
    public void sendHtmlEmail(String email, String subject, String body) {
        Properties properties = System.getProperties();
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AppConstants.EMAIL_ADDRESS, AppConstants.PASSWORD);
            }
        });
        properties.setProperty("mail.smtp.host", AppConstants.SMTP_SERVER);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(AppConstants.EMAIL_ADDRESS));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");
            Transport.send(message);
           // createEmailSentObject(emailSent, 'S');
        } catch (MessagingException ex) {

        }
    }
}
