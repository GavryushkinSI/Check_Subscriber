//package si.gavryushkin.auth;
//
//import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;
//import sun.jvm.hotspot.debugger.AddressException;
//
//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.swing.*;
//import java.net.PasswordAuthentication;
//import java.util.Properties;
//
///**
// * КОД ДЛЯ ОТПРАВКИ СИГНАЛОВ НА ПОЧТУ
// */
//
//public class Test {
//    private Message message = null;
//    private String SMTP_SERVER = null;
//    private String SMTP_Port = null;
//    private String SMTP_AUTH_USER = null;
//    private String SMTP_AUTH_PWD = null;
//    private String EMAIL_FROM = null;
//    private String REPLY_TO = null;
//
//    Properties pr = new Properties();
//
//    void save() {
//        pr.put("server", dialog.getMail().split("@")[1].equals("yandex.ru") ? "smtp.yandex.ru" : "smtp.gmail.com");
//        pr.put("port", dialog.getMail().split("@")[1].equals("yandex.ru") ? "465" : "465");
//        pr.put("from", dialog.getMail());
//        pr.put("user", dialog.getMail().split("@")[0]);
//        pr.put("pass", dialog.getMailpass());
//        pr.put("to", dialog.getMail());
//        pr.put("replyto", "parsesignal@yandex.ru");
//    }
//
//    void SendEmail(final String emailTo, final String thema) {
//        // Настройка SMTP SSL
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", SMTP_SERVER);
//        properties.put("mail.smtp.port", SMTP_Port);
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        try {
//            Authenticator auth = new EmailAuthenticator(SMTP_AUTH_USER,
//                    SMTP_AUTH_PWD);
//            Session session = Session.getDefaultInstance(properties, auth);
//            session.setDebug(false);
//
//            InternetAddress email_from = new InternetAddress(EMAIL_FROM);
//            InternetAddress email_to = new InternetAddress(emailTo);
//            InternetAddress reply_to = (REPLY_TO != null) ?
//                    new InternetAddress(REPLY_TO) : null;
//            message = new MimeMessage(session);
//            message.setFrom(email_from);
//            message.setRecipient(Message.RecipientType.TO, email_to);
//            message.setSubject(thema);
//            if (reply_to != null)
//                message.setReplyTo(new Address[]{reply_to});
//        } catch (AddressException e) {
//            System.err.println(e.getMessage());
//        } catch (MessagingException e) {
//            System.err.println(e.getMessage());
//        }
//    }
//
//    class EmailAuthenticator extends Authenticator {
//        private String login;
//        private String password;
//
//        public EmailAuthenticator(final String login, final String password) {
//            this.login = login;
//            this.password = password;
//        }
//
//        public PasswordAuthentication getPasswordAuthentication() {
//            return new PasswordAuthentication(login, password);
//        }
//    }
//
//    public MimeBodyPart createFileAttachment(String filepath) {
//        // Создание MimeBodyPart
//        MimeBodyPart mbp = new MimeBodyPart();
//
//        // Определение файла в качестве контента
//        FileDataSource fds = new FileDataSource(filepath);
//        try {
//            mbp.setDataHandler(new DataHandler(fds));
//            mbp.setFileName(fds.getName());
//
//        } catch (MessagingException e) {
//            JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка прикрепления файла");
//        }
//        return mbp;
//    }
//
//
//    public boolean sendMessage(final String content) {
//        boolean result = false;
//        try {
//            // Содержимое сообщения
//            Multipart mmp = new MimeMultipart();
//            // Текст сообщения
//            MimeBodyPart bodyPart = new MimeBodyPart();
//            bodyPart.setContent(content, "text/plain; charset=utf-8");
//            mmp.addBodyPart(bodyPart);
//            //Отправка скрина на почту
//            if (dialog.screenmail.getState() == true) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                createScreenformail();
//                MimeBodyPart mbr = createFileAttachment("screen.jpg");
//                mmp.addBodyPart(mbr);
//            }
//            //Отправка скрина на почту
//            // Определение контента сообщения
//            message.setContent(mmp);
//            // Отправка сообщения
//            Transport.send(message);
//            result = true;
//        } catch (MessagingException e) {
//            System.out.println("Ошибка отправки сообщения");
//            System.err.println(e.getMessage());
//            JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
//        }
//        return result;
//    }
////----------------------------------------------------------------------------------------------------------------------------------
//
//    /**
//     * КОД ОТПРАВКИ СООБЩЕНИЯ
//     *
//     * @throws FileNotFoundException
//     */
//    void sendSignal(String thema, String text) {
//        SMTP_SERVER = pr.getProperty("server");
//        SMTP_Port = pr.getProperty("port");
//        EMAIL_FROM = pr.getProperty("from");
//        SMTP_AUTH_USER = pr.getProperty("user");
//        SMTP_AUTH_PWD = pr.getProperty("pass");
//        REPLY_TO = pr.getProperty("replyto");
//
//
//        String emailTo = pr.getProperty("to");
//
//        SendEmail(emailTo, thema);
//        sendMessage(text);
//        System.out.println("Сообщение отправлено " + EMAIL_FROM);
//    }
//}
