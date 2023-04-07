import java.io.*;
import java.util.*;
import javax.mail.*;

public class QuantumHadoop {

   public static void main(String[] args) throws Exception {
      Properties props = new Properties();
      props.setProperty("mail.host", "smtp.google.com");
      props.setProperty("mail.transport.protocol", "smtp");
      props.setProperty("mail.smtp.auth", "true");
      props.setProperty("mail.smtp.port", "587");
      
      Authenticator auth = new Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("username", "password");
         }
      };
      
      Session session = Session.getInstance(props, auth);
      Transport transport = session.getTransport();
      transport.connect();
      
      String body = "Quantum Hadoop job completed successfully!";
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("sender@gmail.com"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recipient@gmail.com"));
      message.setSubject("Quantum Hadoop Job Status");
      message.setText(body);
      transport.sendMessage(message, message.getAllRecipients());
   }
}
