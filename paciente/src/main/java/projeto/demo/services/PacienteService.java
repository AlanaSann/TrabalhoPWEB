package projeto.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import projeto.demo.config.amqp.Email;
import projeto.demo.model.Endereco;
import projeto.demo.model.Pacientes;
import projeto.demo.repository.EnderecoRepository;
import projeto.demo.repository.PacienteRepository;
import projeto.demo.rules.Rules;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Pacientes cadastarPacientes(Pacientes paciente) {
        paciente.setStatus(true);
        enderecoRepository.save(paciente.getEndereco());
        this.enviarEmaildeCadastro(paciente.getEmail());
        return pacienteRepository.save(paciente);
    }

    public void enviarEmail(String enderecoEmail, String assunto, String body) {
        Email email = new Email();
        email.setDestinatario(enderecoEmail);
        email.setAssunto(assunto);
        email.setBody(body);
        email.setRemetente("gamesrellsmob@gmail.com");
        rabbitTemplate.convertAndSend("pacientQueue", email);
    }

    public void enviarEmaildeCadastro(String enderecoEmail) {
        this.enviarEmail(enderecoEmail, "cadastro paciente", "Foi cadastrado");
    }

    public Page<Pacientes> listarPacientes(Pageable pageable) {
        return pacienteRepository.findAllByStatus(true, pageable);
    }

    public void deletarPacientes(Long id) {
        Pacientes pacienteAserExcluido = encontrarPaciente(id);
        pacienteAserExcluido.setStatus(false);
        pacienteRepository.save(pacienteAserExcluido);
    }

    public Pacientes encontrarPaciente(Long id) {
        Optional<Pacientes> paciente = pacienteRepository.findByIdAndStatus(id, true);
        if (paciente.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Paciente não existe");
        }
        return paciente.get();
    }

    public Pacientes encontrarPaciente(String cpf) {
        Optional<Pacientes> paciente = pacienteRepository.findByCpfAndStatus(cpf, true);
        if (paciente.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Paciente não existe");
        }
        return paciente.get();
    }

    public Pacientes attPacientes(Long id, Pacientes paciente) {
        Rules regras = new Rules();
        Pacientes pacienteAserEditado = encontrarPaciente(id);
        regras.valida(paciente, pacienteAserEditado);
        pacienteAserEditado.setNome(paciente.getNome());
        pacienteAserEditado.setTelefone(paciente.getTelefone());
        pacienteAserEditado.setEndereco(paciente.getEndereco());
        enderecoRepository.save(pacienteAserEditado.getEndereco());
        pacienteRepository.save(pacienteAserEditado);
        return pacienteAserEditado;
    }

}
