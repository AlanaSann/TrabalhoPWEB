package consulta.projeto.consultas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import consulta.projeto.consultas.config.amqp.Mensagem;
import consulta.projeto.consultas.dto.MedicoDto;
import consulta.projeto.consultas.form.CancelamentoFormulario;
import consulta.projeto.consultas.form.ConsultaFormulario;
import consulta.projeto.consultas.http.FeignClientMedico;
import consulta.projeto.consultas.http.FeignClientPaciente;
import consulta.projeto.consultas.model.Consulta;
import consulta.projeto.consultas.repository.ConsultaRepository;
import consulta.projeto.consultas.validation.AgendarValidacao;
import consulta.projeto.consultas.validation.CancelarValidacao;
import consulta.projeto.consultas.validation.Validacao;

@Service
public class ConsultaService {
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private ModelMapper conversor;
    @Autowired
    private FeignClientMedico feignClientMedico;
    @Autowired
    private FeignClientPaciente feignClientPaciente;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void enviarMensagem(ConsultaFormulario consulta) {
        String crm = consulta.getCrm();
        Mensagem mensagem = new Mensagem();
        mensagem.setCpf(consulta.getCpf());
        mensagem.setCrmMedico(crm);
        mensagem.setDataConsulta(dataConsulta(consulta.getDataHora()));
        rabbitTemplate.convertAndSend("AgendamentosQueue", mensagem);
    }

    public String dataConsulta(LocalDateTime data){
        String dataConsulta = Integer.toString(data.getDayOfMonth());
        dataConsulta += "/" + Integer.toString(data.getMonthValue());
        dataConsulta += "/" + Integer.toString(data.getDayOfYear());
        dataConsulta += " às " + Integer.toString(data.getHour()) + ":" + Integer.toString(data.getMinute());
        return dataConsulta;
    }
    public void cancelarConsulta(CancelamentoFormulario cancela) {
        Consulta consulta = consultaExiste(cancela.getConsultaId());
        antecedencia24Horas(consulta);
        consulta.setMotivoCancelamento(cancela.getMotivo());
        consultaRepository.save(consulta);
    }

    public void antecedencia24Horas(Consulta consulta) {
        CancelarValidacao cancelarValidacao = new CancelarValidacao(consulta);
        cancelarValidacao.validar();
    }

    public Consulta consultaExiste(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404)));
    }

    public Consulta agendarConsulta(ConsultaFormulario consulta) {

        if (escolheMedico(consulta.getCrm())) {
            consulta.setCrm(escolherMedicoAleatorio(consulta.getDataHora()));
        } else {
             medicoExiste(consulta.getCrm());
             medicoTemDisponibilidadeNessaHora(consulta.getCrm(),
             consulta.getDataHora());
        }
        pacienteExiste(consulta.getCpf());
        unicaConsultaDoDia(consulta.getCpf(), consulta.getDataHora().withHour(0));
        this.enviarMensagem(consulta);

        return consultaRepository.save(conversor.map(consulta, Consulta.class));
    }

    private boolean escolheMedico(String crm) {
        return crm == null;
    }

    private List<MedicoDto> listarMedicos() {
        try {
            return feignClientMedico.listarMedicos();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Serviço de medico offline");
        }
    }

    private String escolherMedicoAleatorio(LocalDateTime data) {
        List<String> lista = listarMedicos().stream().map(m -> m.getCrm()).collect(Collectors.toList());
        List<String> medicosIndisponiveis = consultaRepository.medicosIndisponiveis(data.minusHours(1),
                data.plusHours(1));
        lista.removeAll(medicosIndisponiveis);
        Random random = new Random();
        if (lista.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "equipe medica indisponivel");
        }
        return lista.get(random.nextInt(0, lista.size()));
    }

    private void unicaConsultaDoDia(String cpf, LocalDateTime data) {
        if (consultaRepository.jaTemConsulta(cpf, data) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "paciente não pode marcar mais de uma consulta por dia");
        }
    }

    private void medicoTemDisponibilidadeNessaHora(String crm, LocalDateTime data) {
        if (consultaRepository.jaTemConsultaNessaHora(crm, data.minusHours(1), data.plusHours(1)) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medico não tem disponibilidade nessa hora");
        }
    }

    private void medicoExiste(String crm) {
        try {
            feignClientMedico.medicoExiste(crm);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medico não está cadastrado");
        }
    }

    private void pacienteExiste(String cpf) {
        try {
            feignClientPaciente.pacienteExiste(cpf);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paciente não está cadastrado");
        }
    }

    public void consultaValidacao(ConsultaFormulario consulta) {
        Validacao validacao = new AgendarValidacao(consulta);
        validacao.validar();
    }
}
