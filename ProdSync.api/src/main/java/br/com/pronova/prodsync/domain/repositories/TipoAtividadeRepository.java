package br.com.pronova.prodsync.domain.repositories;

import br.com.pronova.prodsync.domain.entities.TipoAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoAtividadeRepository extends JpaRepository<TipoAtividade, Long> {
    Optional<TipoAtividade> findFirstByNome(String nome);
}

