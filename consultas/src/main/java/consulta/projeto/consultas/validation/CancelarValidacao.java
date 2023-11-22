package consulta.projeto.consultas.validation;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import consulta.projeto.consultas.model.Consulta;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CancelarValidacao implements Validacao{

    private Consulta consulta;

    @Override
    public void validar() {
       tem24HrsAntecedencia();
    }
     private void tem24HrsAntecedencia(){
        if (LocalDateTime.now().plusDays(1).isAfter(consulta.getDataHora())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400),"As consultas devem ser canceladas com antecedência de 24 horas");
        }
    }
}
