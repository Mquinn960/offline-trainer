package com.mquinn.trainer;

import sun.rmi.runtime.Log;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

public class EmailNotifier {

    private Session session;
    private Properties props;

    public EmailNotifier(){

        props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("mq.sl.trainer@gmail.com","MyPassword1!");
                    }
                });

    }

    public void sendNotification(String messageText, String eventDescription){

      try {

          Message message = new MimeMessage(session);

          message.setFrom(new InternetAddress("mq.sl.trainer@gmail.com"));

          message.setRecipients(Message.RecipientType.TO,
                  InternetAddress.parse("matt@mquinn.co.uk"));

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
