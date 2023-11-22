package consulta.projeto.consultas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import consulta.projeto.consultas.form.CancelamentoFormulario;
import consulta.projeto.consultas.form.ConsultaFormulario;
import consulta.projeto.consultas.model.Consulta;
import consulta.projeto.consultas.service.ConsultaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {
    @Autowired  //(injetar)"instanciar" o consulta
    private ConsultaService consultaService;

    @PostMapping("/agendar")
    public ResponseEntity<ConsultaFormulario> agendar(@Valid @RequestBody ConsultaFormulario consulta){ 
        consultaService.consultaValidacao(consulta);
        consultaService.agendarConsulta(consulta);
        return ResponseEntity.ok().body(consulta);
    }

    @DeleteMapping("/cancelar")
    public ResponseEntity<CancelamentoFormulario> cancelar(@Valid @RequestBody CancelamentoFormulario cancelamento){
        consultaService.cancelarConsulta(cancelamento);
        return ResponseEntity.ok().body(cancelamento);
    }
    
}
