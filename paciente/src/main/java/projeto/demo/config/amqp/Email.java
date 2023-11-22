package projeto.demo.config.amqp;

import java.io.Serializable;

import lombok.Data;

@Data
public class Email implements Serializable {
    private String assunto;
    private String body;
    private String destinatario;
    private String remetente;

}
