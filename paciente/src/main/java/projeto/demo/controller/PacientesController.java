package projeto.demo.controller;
import java.util.List;

import javax.swing.SortOrder;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import projeto.demo.dto.PacienteDto;
import projeto.demo.form.PacienteForm;
import projeto.demo.model.Pacientes;
import projeto.demo.services.PacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacientesController {
    
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/cadastrar")
    public ResponseEntity<PacienteDto> cadastrar(@Valid @RequestBody PacienteForm paciente){
        Pacientes pacienteConvertido = modelMapper.map(paciente, Pacientes.class);
        pacienteService.cadastarPacientes(pacienteConvertido);
        PacienteDto pacienteDto = modelMapper.map(pacienteConvertido, PacienteDto.class);
        return ResponseEntity.created(null).body(pacienteDto);
    }
    
    @GetMapping("/listar/{page}")
    public ResponseEntity<Page<PacienteDto>> listar(@PathVariable int page){
        Pageable pageable = PageRequest.of(page, 10,Sort.by(Sort.Direction.ASC,"nome"));
        return ResponseEntity.ok().body(pacienteService.listarPacientes(pageable).map(p-> modelMapper.map(p,PacienteDto.class)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDto> getPaciente(@PathVariable Long id){
        return ResponseEntity.ok().body(modelMapper.map(pacienteService.encontrarPaciente(id),PacienteDto.class));
    }

    @GetMapping("/buscar/{cpf}")
    public ResponseEntity<PacienteDto> buscarPorCpf(@PathVariable String cpf){
        return ResponseEntity.ok().body(modelMapper.map(pacienteService.encontrarPaciente(cpf),PacienteDto.class));
    }

    @DeleteMapping("/deletar/{id}")
    public Object deletarPacientes(@PathVariable Long id){
        pacienteService.deletarPacientes(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<PacienteDto> atualizarPacientes(@PathVariable Long id,@Valid @RequestBody PacienteForm paciente){
        Pacientes pacienteConvertido = modelMapper.map(paciente, Pacientes.class);
        pacienteService.attPacientes(id, pacienteConvertido);
        PacienteDto pacienteDto = modelMapper.map(pacienteConvertido,PacienteDto.class);
        return ResponseEntity.created(null).body(pacienteDto);
    }
}
