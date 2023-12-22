package listener;

import entity.Users;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.Date;
import java.util.Properties;

public class EmailSend{
    private static final String ALPHA_NUMERIC_STRING = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

    public EmailSend(String email){
        String token=randomAlphaNumeric(32);

        String host ="smtp.live.com" ;
        String user = "qwirktrapani@hotmail.com";
        String pass = "QwirkIsTheFirst1!";
        String to = email;
        String from = "qwirktrapani@hotmail.com";
        String subject = "Reset Your Password";

        boolean sessionDebug = false;

        Properties props = System.getProperties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.required", "true");

        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(sessionDebug);
        Message msg = new MimeMessage(mailSession);

        try{
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject); msg.setSentDate(new Date());
            msg.setContent("<html>\n" + "<body>\n" + "<h1>Qwirk IM</h1>\n" + "\n" +
                    //"<img src=\"E:\\anguria.jpg\" alt=\"Qwirk IM\" style=\"width:50px;height:50px;\">\n" +
                    "Token: "+token+"\n" + "\n" +
                    "<h2>Use it to change your password on Qwirk</h2>\n" +
                    "\n" + "</body>\n" + "</html>", "text/html");

            Transport transport=mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

            Users.update_token(token,email);

            JOptionPane.showMessageDialog(null, "Check your email!");
            System.out.println("message send successfully");
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            builder.append(ALPHA_NUMERIC_STRING.charAt((int) (Math.random()*ALPHA_NUMERIC_STRING.length())));
        }
        return builder.toString();
    }
}
