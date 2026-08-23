package br.com.pronova.prodsync.application.dtos;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DossieColaboradorDTO {
    
    private Long id;
    private String nome;
    
    // 1. Radar Chart Metrics (Comparativo com a media do galpao)
    private RadarMetrics radar;
    
    // 2. Hit Rate (Quantos % dos dias bateu qual meta)
    private HitRateMetrics hitRate;
    
    // 3. Absenteismo (Faltas agrupadas por dia da semana)
    private List<FaltaDiaSemanaDTO> absenteismo;

    @Data
    @Builder
    public static class RadarMetrics {
        private Double mediaCaixasColaborador;
        private Double mediaCaixasEmpresa;
        private Double mediaVisitasColaborador;
        private Double mediaVisitasEmpresa;
        private Double assiduidadeColaborador; 
        private Double assiduidadeEmpresa;
    }

    @Data
    @Builder
    public static class HitRateMetrics {
        private int diasAzul;
        private int diasVerde;
        private int diasAmarelo;
        private int diasSemMeta;
        private int diasTotal;
    }

    @Data
    @Builder
    public static class FaltaDiaSemanaDTO {
        private String diaSemana;
        private int totalFaltas;
    }
}