package projeto.demo.config.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import projeto.demo.model.Pacientes;
import projeto.demo.services.PacienteService;

@Component
public class FilasListener {

    @Autowired
    private PacienteService pacienteService;

    @RabbitListener(queues = "AgendamentosQueue")
    public void listenerMensagens(Mensagem mensagem) {
        Pacientes paciente = pacienteService.encontrarPaciente((mensagem.getCpf()));
        System.out.println(mensagem.getCpf());
        pacienteService.enviarEmail(paciente.getEmail(), "Marcação de Consulta",
                paciente.getNome() +" sua consulta foi agendada" + "\nData:" + mensagem.getDataConsulta() +"\nCrm do Medico: " + mensagem.getCrmMedico());

    }
}
