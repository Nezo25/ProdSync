package br.com.pronova.prodsync.domain.repositories;

import br.com.pronova.prodsync.domain.entities.RegistroProdutividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroProdutividadeRepository extends JpaRepository<RegistroProdutividade, Long> {

    @Query("SELECT r FROM RegistroProdutividade r WHERE r.dataHora BETWEEN :inicio AND :fim")
    List<RegistroProdutividade> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT COUNT(DISTINCT r.colaborador.id) FROM RegistroProdutividade r WHERE r.faixaHoraria = :faixaHoraria AND r.dataHora >= :data")
    Long countHeadcountByFaixaHorariaAndData(@Param("faixaHoraria") Integer faixaHoraria, @Param("data") LocalDateTime data);

    @Query(value = "SELECT DATE(r.data_hora) as dataReferencia, r.colaborador_id as colaboradorId, " +
                   "SUM(r.quantidade_primaria) as totalCaixas, COUNT(r.id) as horasTrabalhadas, " +
                   "SUM(r.quantidade_secundaria) as totalVisitas " +
                   "FROM registros_produtividade r " +
                   "WHERE r.data_hora BETWEEN :inicio AND :fim " +
                   "GROUP BY DATE(r.data_hora), r.colaborador_id", nativeQuery = true)
    List<Object[]> getAgregacaoDiariaPorColaborador(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
