package br.com.pronova.prodsync.domain.repositories;

import br.com.pronova.prodsync.domain.entities.ChamadoReabastecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoReabastecimentoRepository extends JpaRepository<ChamadoReabastecimento, Long> {
    List<ChamadoReabastecimento> findByStatus(String status);
    List<ChamadoReabastecimento> findByEmpilhadeiristaIdAndStatus(Long empilhadeiristaId, String status);
}
