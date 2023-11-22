package consulta.projeto.consultas.form;

import consulta.projeto.consultas.model.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelamentoFormulario {
    @NotNull
    private Long consultaId;
    @NotNull
    private MotivoCancelamento motivo;
}
