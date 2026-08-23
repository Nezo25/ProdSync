package br.com.pronova.prodsync.application.services;

import br.com.pronova.prodsync.application.dtos.DashboardMetricsDTO;
import br.com.pronova.prodsync.domain.entities.RegistroProdutividade;
import br.com.pronova.prodsync.domain.repositories.RegistroProdutividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutividadeService {

    private final RegistroProdutividadeRepository registroRepository;

    @org.springframework.cache.annotation.Cacheable(value = "metricas_produtividade", key = "#data.toString() + '-' + #faixaHoraria")
    public DashboardMetricsDTO obterMetricasPorFaixaHoraria(Integer faixaHoraria, LocalDateTime data) {
        Long headcount = registroRepository.countHeadcountByFaixaHorariaAndData(faixaHoraria, data);
        
        LocalDateTime inicio = data.withHour(faixaHoraria).withMinute(0).withSecond(0);
        LocalDateTime fim = data.withHour(faixaHoraria).withMinute(59).withSecond(59);
        
        List<RegistroProdutividade> registros = registroRepository.findByDataHoraBetween(inicio, fim);
        
        Double totalMovimentacoes = registros.stream()
                .mapToDouble(RegistroProdutividade::getQuantidadePrimaria)
                .sum();
                
        Double mediaProdutividade = headcount > 0 ? totalMovimentacoes / headcount : 0.0;
        
        return new DashboardMetricsDTO(faixaHoraria, headcount, totalMovimentacoes, mediaProdutividade);
    }
}
