package projeto.demo.dto;

import lombok.Getter;
import lombok.Setter;
import projeto.demo.model.Endereco;

@Getter
@Setter
public class PacienteDto {
    private String nome;
    private String email;
    private String cpf;
}
