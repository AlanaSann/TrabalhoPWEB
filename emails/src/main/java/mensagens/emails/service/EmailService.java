package mensagens.emails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import mensagens.emails.model.Email;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void enviarEmail(Email email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email.getRemetente());
        message.setTo(email.getDestinatario());
        message.setSubject(email.getAssunto());
        message.setText(email.getBody());
        emailSender.send(message);
    }
}
