package br.com.pronova.prodsync.domain.repositories;

import br.com.pronova.prodsync.domain.entities.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    Optional<Colaborador> findFirstByNome(String nome);
}

