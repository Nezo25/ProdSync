package br.com.pronova.prodsync.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMatrizDTO {
    private Long id;
    private String nome;
    private Double horasTrabalhadas;
    private Map<Integer, RegistroHoraDTO> registros;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegistroHoraDTO {
        private Double caixa;
        private Double visita;
    }
}
