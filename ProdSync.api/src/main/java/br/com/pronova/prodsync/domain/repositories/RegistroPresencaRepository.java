package br.com.pronova.prodsync.domain.repositories;

import br.com.pronova.prodsync.domain.entities.RegistroPresenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroPresencaRepository extends JpaRepository<RegistroPresenca, Long> {
    List<RegistroPresenca> findByDataReferencia(LocalDate dataReferencia);
    List<RegistroPresenca> findByDataReferenciaBetween(LocalDate start, LocalDate end);
}
