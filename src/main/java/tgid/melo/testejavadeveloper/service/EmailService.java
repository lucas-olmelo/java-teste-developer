package tgid.melo.testejavadeveloper.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    private final JavaMailSender mailSender;

    public void sendMail(String para, String assunto, String conteudo){
        var message = new SimpleMailMessage();
        message.setTo(para);
        message.setSubject(assunto);
        message.setText(conteudo);
        mailSender.send(message);
    }

}
