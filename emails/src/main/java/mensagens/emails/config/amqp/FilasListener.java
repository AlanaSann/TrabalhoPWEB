package mensagens.emails.config.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mensagens.emails.model.Email;
import mensagens.emails.service.EmailService;

@Component
public class FilasListener {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "pacientQueue")
    public void listenerPacientes(Email email) {
        emailService.enviarEmail(email);
    }
}
