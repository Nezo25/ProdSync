package br.com.pronova.prodsync.application.services;

import br.com.pronova.prodsync.application.dtos.CustoOperacaoDTO;
import br.com.pronova.prodsync.application.dtos.DossieColaboradorDTO;
import br.com.pronova.prodsync.domain.repositories.RegistroProdutividadeRepository;
import br.com.pronova.prodsync.domain.repositories.RegistroPresencaRepository;
import br.com.pronova.prodsync.domain.repositories.ColaboradorRepository;
import br.com.pronova.prodsync.domain.entities.Colaborador;
import br.com.pronova.prodsync.domain.entities.RegistroPresenca;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BiDashboardService {

    private final RegistroProdutividadeRepository registroRepository;
    private final RegistroPresencaRepository presencaRepository;
    private final ColaboradorRepository colaboradorRepository;

    public CustoOperacaoDTO getCustoOperacaoMensal(Integer ano, Integer mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDateTime inicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime fim = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // [data_referencia, colaborador_id, total_caixas, horas_trabalhadas, total_visitas]
        List<Object[]> agregacao = registroRepository.getAgregacaoDiariaPorColaborador(inicio, fim);

        Map<LocalDate, CustoOperacaoDTO.CustoDiarioDTO> mapDiario = new TreeMap<>();
        double custoMensalAcumulado = 0.0;
        double totalCaixasMes = 0.0;
        double totalHorasMes = 0.0;

        for (Object[] row : agregacao) {
            Object dateObj = row[0];
            LocalDate data = dateObj instanceof java.sql.Date 
                             ? ((java.sql.Date) dateObj).toLocalDate() 
                             : (LocalDate) dateObj;
                             
            Long colaboradorId = ((Number) row[1]).longValue();
            Double totalCaixas = ((Number) row[2]).doubleValue();
            Integer horasTrabalhadas = ((Number) row[3]).intValue();
            Double totalVisitas = row[4] != null ? ((Number) row[4]).doubleValue() : 0.0;

            double mediaCaixasHora = horasTrabalhadas > 0 ? totalCaixas / horasTrabalhadas : 0;
            double mediaVisitasHora = horasTrabalhadas > 0 ? totalVisitas / horasTrabalhadas : 0;
            
            double provisaoBonusDiario = 0.0;
            
            if (totalCaixas >= 350 || totalVisitas >= 30) {
                provisaoBonusDiario = 900.0 / 22.0; // Azul
            } else if (totalCaixas >= 325 || totalVisitas >= 28) {
                provisaoBonusDiario = 700.0 / 22.0; // Verde
            } else if (totalCaixas >= 300 || totalVisitas >= 26) {
                provisaoBonusDiario = 500.0 / 22.0; // Amarelo
            }

            CustoOperacaoDTO.CustoDiarioDTO diarioDTO = mapDiario.computeIfAbsent(data, d -> 
                CustoOperacaoDTO.CustoDiarioDTO.builder()
                    .data(d)
                    .totalCaixas(0.0)
                    .custoBonus(0.0)
                    .headcount(0)
                    .build()
            );

            diarioDTO.setTotalCaixas(diarioDTO.getTotalCaixas() + totalCaixas);
            diarioDTO.setCustoBonus(diarioDTO.getCustoBonus() + provisaoBonusDiario);
            diarioDTO.setHeadcount(diarioDTO.getHeadcount() + 1);

            custoMensalAcumulado += provisaoBonusDiario;
            totalCaixasMes += totalCaixas;
            totalHorasMes += horasTrabalhadas;
        }

        return CustoOperacaoDTO.builder()
                .custoBonusAcumulado(Math.round(custoMensalAcumulado * 100.0) / 100.0)
                .totalCaixasMes(totalCaixasMes)
                .mediaCaixasHoraMes(totalHorasMes > 0 ? Math.round(totalCaixasMes / totalHorasMes * 10.0) / 10.0 : 0.0)
                .dias(new ArrayList<>(mapDiario.values()))
                .build();
    }

    public DossieColaboradorDTO getDossieColaborador(Long id, Integer ano, Integer mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate startMonth = yearMonth.atDay(1);
        LocalDate endMonth = yearMonth.atEndOfMonth();
        LocalDateTime inicio = startMonth.atStartOfDay();
        LocalDateTime fim = endMonth.atTime(23, 59, 59);

        // Agregação de produtividade (toda a empresa)
        List<Object[]> agregacaoEmpresa = registroRepository.getAgregacaoDiariaPorColaborador(inicio, fim);

        double somaCaixasEmpresa = 0;
        double somaVisitasEmpresa = 0;
        int horasTrabalhadasEmpresa = 0;

        for (Object[] row : agregacaoEmpresa) {
            somaCaixasEmpresa += ((Number) row[2]).doubleValue();
            horasTrabalhadasEmpresa += ((Number) row[3]).intValue();
            somaVisitasEmpresa += row[4] != null ? ((Number) row[4]).doubleValue() : 0.0;
        }

        double mediaCaixasEmpresa = horasTrabalhadasEmpresa > 0 ? somaCaixasEmpresa / horasTrabalhadasEmpresa : 0;
        double mediaVisitasEmpresa = horasTrabalhadasEmpresa > 0 ? somaVisitasEmpresa / horasTrabalhadasEmpresa : 0;

        // Faltas (toda a empresa)
        List<RegistroPresenca> presencasEmpresa = presencaRepository.findByDataReferenciaBetween(startMonth, endMonth);
        long totalPresencasEmpresa = presencasEmpresa.size();
        long faltasEmpresa = presencasEmpresa.stream()
                .filter(p -> p.getStatusPresenca().name().equals("FALTA"))
                .count();
        double assiduidadeEmpresa = totalPresencasEmpresa > 0 
                ? ((double) (totalPresencasEmpresa - faltasEmpresa) / totalPresencasEmpresa) * 100.0 
                : 100.0;

        // Filtro do Colaborador (ou Geral se id == 0)
        List<Object[]> agregacaoColaborador = agregacaoEmpresa;
        List<RegistroPresenca> presencasColaborador = presencasEmpresa;
        String nomeColaborador = "Geral (Toda a Operação)";

        if (id != 0) {
            agregacaoColaborador = agregacaoEmpresa.stream()
                .filter(row -> ((Number) row[1]).longValue() == id)
                .collect(Collectors.toList());
                
            presencasColaborador = presencasEmpresa.stream()
                .filter(p -> p.getColaborador().getId().equals(id))
                .collect(Collectors.toList());
                
            Colaborador colab = colaboradorRepository.findById(id).orElse(null);
            if (colab != null) {
                nomeColaborador = colab.getNome();
            }
        }

        // Métricas do Colaborador (Radar & Hit Rate)
        double somaCaixasColab = 0;
        double somaVisitasColab = 0;
        int horasTrabalhadasColab = 0;
        
        int diasAzul = 0;
        int diasVerde = 0;
        int diasAmarelo = 0;
        int diasSemMeta = 0;

        for (Object[] row : agregacaoColaborador) {
            double totalCaixas = ((Number) row[2]).doubleValue();
            int horas = ((Number) row[3]).intValue();
            double totalVisitas = row[4] != null ? ((Number) row[4]).doubleValue() : 0.0;

            double cxHora = horas > 0 ? totalCaixas / horas : 0;
            double viHora = horas > 0 ? totalVisitas / horas : 0;

            somaCaixasColab += totalCaixas;
            somaVisitasColab += totalVisitas;
            horasTrabalhadasColab += horas;

            if (totalCaixas >= 350 || totalVisitas >= 30) {
                diasAzul++;
            } else if (totalCaixas >= 325 || totalVisitas >= 28) {
                diasVerde++;
            } else if (totalCaixas >= 300 || totalVisitas >= 26) {
                diasAmarelo++;
            } else {
                diasSemMeta++;
            }
        }

        double mediaCaixasColab = horasTrabalhadasColab > 0 ? somaCaixasColab / horasTrabalhadasColab : 0;
        double mediaVisitasColab = horasTrabalhadasColab > 0 ? somaVisitasColab / horasTrabalhadasColab : 0;
        int totalDias = diasAzul + diasVerde + diasAmarelo + diasSemMeta;

        // Absenteísmo do Colaborador (ou Geral)
        long totalPresencasColab = presencasColaborador.size();
        long faltasColab = presencasColaborador.stream()
                .filter(p -> p.getStatusPresenca().name().equals("FALTA"))
                .count();
        double assiduidadeColaborador = totalPresencasColab > 0 
                ? ((double) (totalPresencasColab - faltasColab) / totalPresencasColab) * 100.0 
                : 100.0;
                
        // Agrupar faltas por dia da semana
        Map<DayOfWeek, Integer> faltasPorDia = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek d : DayOfWeek.values()) {
            faltasPorDia.put(d, 0);
        }
        
        for (RegistroPresenca p : presencasColaborador) {
            if (p.getStatusPresenca().name().equals("FALTA")) {
                DayOfWeek dia = p.getDataReferencia().getDayOfWeek();
                faltasPorDia.put(dia, faltasPorDia.get(dia) + 1);
            }
        }

        List<DossieColaboradorDTO.FaltaDiaSemanaDTO> absenteismoList = new ArrayList<>();
        for (Map.Entry<DayOfWeek, Integer> entry : faltasPorDia.entrySet()) {
            if (entry.getValue() > 0) {
                String nomeDia = entry.getKey().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
                // Capitalize first letter
                nomeDia = nomeDia.substring(0, 1).toUpperCase() + nomeDia.substring(1);
                absenteismoList.add(DossieColaboradorDTO.FaltaDiaSemanaDTO.builder()
                        .diaSemana(nomeDia)
                        .totalFaltas(entry.getValue())
                        .build());
            }
        }
        
        // Se não houver faltas, colocar algo para não quebrar o gráfico? Recharts aceita array vazio.

        return DossieColaboradorDTO.builder()
            .id(id)
            .nome(nomeColaborador)
            .radar(DossieColaboradorDTO.RadarMetrics.builder()
                .mediaCaixasColaborador(Math.round(mediaCaixasColab * 10.0)/10.0)
                .mediaCaixasEmpresa(Math.round(mediaCaixasEmpresa * 10.0)/10.0)
                .mediaVisitasColaborador(Math.round(mediaVisitasColab * 10.0)/10.0)
                .mediaVisitasEmpresa(Math.round(mediaVisitasEmpresa * 10.0)/10.0)
                .assiduidadeColaborador(Math.round(assiduidadeColaborador * 10.0)/10.0)
                .assiduidadeEmpresa(Math.round(assiduidadeEmpresa * 10.0)/10.0)
                .build())
            .hitRate(DossieColaboradorDTO.HitRateMetrics.builder()
                .diasAzul(diasAzul).diasVerde(diasVerde).diasAmarelo(diasAmarelo).diasSemMeta(diasSemMeta).diasTotal(totalDias)
                .build())
            .absenteismo(absenteismoList)
            .build();
    }
}
