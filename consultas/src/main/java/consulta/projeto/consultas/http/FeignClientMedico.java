package consulta.projeto.consultas.http;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import consulta.projeto.consultas.dto.MedicoDto;

@FeignClient("medico")
public interface FeignClientMedico {

    @RequestMapping(method = RequestMethod.GET, value = "/medicos/buscar/{crm}")
        MedicoDto medicoExiste(@PathVariable("crm") String crm);
    
    @RequestMapping(method = RequestMethod.GET, value = "medicos/listarMedicos")
        List<MedicoDto> listarMedicos();
}