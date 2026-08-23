package br.com.pronova.prodsync.domain.repositories;

import br.com.pronova.prodsync.domain.entities.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    Optional<Turno> findByDescricao(String descricao);
}
