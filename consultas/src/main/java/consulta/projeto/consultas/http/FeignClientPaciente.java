package consulta.projeto.consultas.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import consulta.projeto.consultas.dto.PacienteDto;

@FeignClient("paciente")
public interface FeignClientPaciente {

    @RequestMapping(method = RequestMethod.GET, value = "/pacientes/buscar/{cpf}")
        PacienteDto pacienteExiste(@PathVariable("cpf") String cpf);
    
} 
