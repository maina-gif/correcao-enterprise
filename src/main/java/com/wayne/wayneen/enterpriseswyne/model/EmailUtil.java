package com.wayne.wayneen.enterpriseswyne;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

public class EmailUtil {

    public static void enviarEmailComAnexo(String destinatario, String assunto, String corpo, File anexo) throws Exception {
        final String remetente = "seuemail@gmail.com";
        final String senha = "suasenha";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senha);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);

            MimeBodyPart corpoTexto = new MimeBodyPart();
            corpoTexto.setText(corpo, "UTF-8");

            MimeBodyPart corpoAnexo = new MimeBodyPart();
            corpoAnexo.attachFile(anexo);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(corpoTexto);
            multipart.addBodyPart(corpoAnexo);

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("✅ E-mail enviado com sucesso para: " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new Exception("Erro ao enviar e-mail", e);
        }
    }
}
