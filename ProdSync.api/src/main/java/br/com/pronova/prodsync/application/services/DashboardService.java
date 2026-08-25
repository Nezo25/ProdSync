package br.com.pronova.prodsync.application.services;

import br.com.pronova.prodsync.application.dtos.DashboardMatrizDTO;
import br.com.pronova.prodsync.domain.entities.RegistroProdutividade;
import br.com.pronova.prodsync.domain.entities.Colaborador;
import br.com.pronova.prodsync.domain.repositories.ColaboradorRepository;
import br.com.pronova.prodsync.domain.repositories.RegistroProdutividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RegistroProdutividadeRepository registroRepository;
    private final PresencaService presencaService;
    private final ColaboradorRepository colaboradorRepository;

    public List<DashboardMatrizDTO> getMatrizPicking(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);

        List<RegistroProdutividade> registros = registroRepository.findByDataHoraBetween(inicio, fim);
        Map<Long, Double> horasTrabalhadasMap = presencaService.getHorasTrabalhadasPorColaborador(data);
        List<Colaborador> todosColaboradores = colaboradorRepository.findAll(); // No futuro: findByAtivoTrue()

        Map<Long, DashboardMatrizDTO> matrizMap = new HashMap<>();

        // 1. Inicializa todos os colaboradores na matriz
        for (Colaborador c : todosColaboradores) {
            Double horasTrabalhadas = horasTrabalhadasMap.getOrDefault(c.getId(), 8.0);
            matrizMap.put(c.getId(), DashboardMatrizDTO.builder()
                    .id(c.getId())
                    .nome(c.getNome())
                    .horasTrabalhadas(horasTrabalhadas)
                    .registros(new HashMap<>())
                    .build());
        }

        // 2. Preenche a produtividade
        for (RegistroProdutividade r : registros) {
            Long colabId = r.getColaborador().getId();
            Integer hora = r.getFaixaHoraria();

            DashboardMatrizDTO dto = matrizMap.get(colabId);
            if (dto == null) continue;

            DashboardMatrizDTO.RegistroHoraDTO horaDTO = dto.getRegistros().computeIfAbsent(hora, k -> 
                new DashboardMatrizDTO.RegistroHoraDTO(0.0, 0.0)
            );

            Double primary = r.getQuantidadePrimaria() != null ? r.getQuantidadePrimaria() : 0.0;
            Double secondary = r.getQuantidadeSecundaria() != null ? r.getQuantidadeSecundaria() : 0.0;

            String nomeAtividade = r.getTipoAtividade() != null ? r.getTipoAtividade().getNome() : "";
            
            if ("Visitas".equalsIgnoreCase(nomeAtividade) || "Visita".equalsIgnoreCase(nomeAtividade)) {
                horaDTO.setVisita(horaDTO.getVisita() + primary);
                horaDTO.setCaixa(horaDTO.getCaixa() + secondary); // Apenas fallback
            } else {
                horaDTO.setCaixa(horaDTO.getCaixa() + primary);
                horaDTO.setVisita(horaDTO.getVisita() + secondary);
            }
        }

        return new ArrayList<>(matrizMap.values());
    }
}

