package consulta.projeto.consultas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import consulta.projeto.consultas.model.Consulta;
// Conecta o banco com a aplicação
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta,Long>{
    @Query(value = "select * from consulta where cpf = :cpfPaciente and data_Hora >= :dataHora",nativeQuery = true)
    public Consulta jaTemConsulta(@Param("cpfPaciente")String cpf,@Param("dataHora") LocalDateTime data);

    @Query(value = "select * from consulta where crm = :crmMedico and data_Hora > :inicio and data_Hora < :fim",nativeQuery =  true)
    public Consulta jaTemConsultaNessaHora(@Param("crmMedico")String crm,@Param("inicio")LocalDateTime inicio,@Param("fim")LocalDateTime fim);

    @Query(value = "select * from consulta where data_Hora > :inicio and data_Hora < :fim",nativeQuery = true)
    public List<String> medicosIndisponiveis(@Param("inicio")LocalDateTime inicio,@Param("fim")LocalDateTime fim);
}
