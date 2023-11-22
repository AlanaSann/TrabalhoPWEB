package consulta.projeto.consultas.validation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import consulta.projeto.consultas.form.ConsultaFormulario;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AgendarValidacao implements Validacao{
    private ConsultaFormulario consultaFormulario;

    @Override
    public void validar() {
      diaFuncionamentoClinica();
      duracaoConsulta();
      tem30MinAntecedencia();
    }

    private void diaFuncionamentoClinica(){
        if (consultaFormulario.getDataHora().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400),"Dia da semana indisponivel"); 
        }
    }

    private void duracaoConsulta(){
        if (consultaFormulario.getDataHora().plusHours(1).isAfter(consultaFormulario.getDataHora().withHour(19).withMinute(0))) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400),"Horário indisponivel"); 
        }
    }

    private void tem30MinAntecedencia(){
        if (LocalDateTime.now().plusMinutes(30).isAfter(consultaFormulario.getDataHora())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400),"As consultas devem ser agendadas com antecedência mínima de 30 minutos");
        }
    }

    
    
    
}
