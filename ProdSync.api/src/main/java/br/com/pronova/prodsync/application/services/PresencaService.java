package br.com.pronova.prodsync.application.services;

import br.com.pronova.prodsync.domain.entities.RegistroPresenca;
import br.com.pronova.prodsync.domain.enums.StatusPresenca;
import br.com.pronova.prodsync.domain.repositories.RegistroPresencaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PresencaService {

    private final RegistroPresencaRepository presencaRepository;

    public Map<Long, Double> getHorasTrabalhadasPorColaborador(LocalDate data) {
        List<RegistroPresenca> presencas = presencaRepository.findByDataReferencia(data);
        
        return presencas.stream()
                .collect(Collectors.toMap(
                        p -> p.getColaborador().getId(),
                        this::calcularHorasTrabalhadasReais
                ));
    }

    public double calcularHorasTrabalhadasReais(RegistroPresenca presenca) {
        if (presenca.getStatusPresenca() != StatusPresenca.PRESENTE) {
            return 0.0;
        }

        LocalTime entrada = presenca.getHoraEntrada();
        LocalTime saida = presenca.getHoraSaida();
        LocalTime inicioPausa = presenca.getHoraInicioPausa();
        LocalTime fimPausa = presenca.getHoraFimPausa();

        if (entrada == null || saida == null) {
            return 0.0;
        }

        // Calcula o tempo total logado no galpão
        Duration tempoTotal = Duration.between(entrada, saida);

        long minutosReais = tempoTotal.toMinutes();

        // Se houver pausa registrada e ela estiver dentro do turno
        if (inicioPausa != null && fimPausa != null) {
            Duration tempoAlmoco = Duration.between(inicioPausa, fimPausa);
            minutosReais -= tempoAlmoco.toMinutes();
        }

        // Guard Clause: Proteção contra divisão por zero (ArithmeticException)
        if (minutosReais <= 0) {
            return 0.0;
        }

        // Converte os minutos de volta para horas decimais (ex: 450 min = 7.5 horas)
        return minutosReais / 60.0;
    }
}
