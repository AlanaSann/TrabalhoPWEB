package projeto.demo.form;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PacienteForm {

    @NotBlank(message = "Campo obrigatório")
    @Pattern(message = "Não inserir numeros",regexp ="[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$")
    private String nome;
    
    @NotBlank(message = "Campo obrigatório")
    @Email(message = "Email invalido",regexp ="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotBlank(message = "Campo obrigatório")
    @Pattern(message = "Não inserir letras",regexp = "^[0-9]+$")
    private String telefone;

    @NotBlank(message = "Campo obrigatório")
    @CPF(message = "CPF invalido")
    private String cpf;

    @Valid
    private EnderecoForm endereco;

    @JsonCreator
    public PacienteForm(@JsonProperty("nome") String nome,@JsonProperty("email") String email,@JsonProperty("telefone") String telefone,
    @JsonProperty("cpf") String cpf,@JsonProperty("endereco") EnderecoForm endereco){
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.endereco = endereco;
    }
    
}
