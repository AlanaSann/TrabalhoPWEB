package mensagens.emails.model;

import lombok.Data;

@Data
public class Email {
    private String assunto;
    private String body;
    private String destinatario;
    private String remetente;

}
