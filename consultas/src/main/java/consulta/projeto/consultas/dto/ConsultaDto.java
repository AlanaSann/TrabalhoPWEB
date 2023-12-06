package consulta.projeto.consultas.dto;

import java.time.LocalDateTime;

import consulta.projeto.consultas.model.Consulta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaDto {
    private Long idConsulta;
    private String crmMedico;
    private String cpfPaciente;
    private LocalDateTime horarioConsulta;

    public ConsultaDto(Consulta consulta) {
        this.idConsulta = consulta.getId();
        this.crmMedico = consulta.getCrm();
        this.cpfPaciente = consulta.getCpf();
        this.horarioConsulta = consulta.getDataHora();
    }

    
}
