package projeto.demo.rules;

import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import projeto.demo.model.Pacientes;

public class Rules {
    public void valida(Pacientes pacienteNovo, Pacientes pacienteAtual){
        validaEmail(pacienteNovo.getEmail(), pacienteAtual.getEmail());
        validaCPF(pacienteNovo.getCpf(), pacienteAtual.getCpf());
    }

    private void validaEmail(String emailNovo, String emailAtual){
        if (!emailNovo.equals(emailAtual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possivel modificar o email");
        }
    }
    private void validaCPF(String cpfNovo,String cpfAtual){
        if (!cpfAtual.equals(cpfNovo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Não é possivel modificar o cpf");   
        }
    }
}
