package br.com.pronova.prodsync.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoOperacaoDTO {
    private Double custoBonusAcumulado;
    private Double totalCaixasMes;
    private Double mediaCaixasHoraMes;
    private List<CustoDiarioDTO> dias;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustoDiarioDTO {
        private LocalDate data;
        private Double totalCaixas;
        private Double custoBonus;
        private Integer headcount;
    }
}
