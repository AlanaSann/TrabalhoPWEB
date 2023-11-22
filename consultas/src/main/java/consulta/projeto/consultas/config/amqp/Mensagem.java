package consulta.projeto.consultas.config.amqp;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
public class Mensagem {
    private String cpf;
    private String crmMedico;
    private String dataConsulta;

}
