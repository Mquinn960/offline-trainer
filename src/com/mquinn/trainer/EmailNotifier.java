package com.mquinn.trainer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailNotifier {

    private Session session;
    private Properties props;

    private final String SMTP_USERNAME = "your.email.address@gmail.com";
    private final String SMTP_PASSWORD = "MyPassword1!";
    private final String SMTP_HOST = "smtp.gmail.com";
    private final String SMTP_PORT = "465";

    private final String EMAIL_FROM = "your.email.address@gmail.com";
    private final String EMAIL_TO = "your.email.address@gmail.com";

    public EmailNotifier(){

        props = new Properties();

        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", SMTP_PORT);

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SMTP_USERNAME,SMTP_PASSWORD);
                    }
                });

    }

    public void sendNotification(String messageText, String eventDescription){

      try {

          Message message = new MimeMessage(session);

          message.setFrom(new InternetAddress(EMAIL_FROM));

          message.setRecipients(Message.RecipientType.TO,
                  InternetAddress.parse(EMAIL_TO));

          message.setSubject(eventDescription);

          message.setText("Hello," +
                          "\n\n" +
                           messageText +
                          "\n\n" +
                          "Good day.");

          Transport.send(message);

          System.out.println("Email notification Sent");

      } catch (
              MessagingException e) {
          Logger.getAnonymousLogger().log(Level.INFO, e.toString());
      }
    }

}
