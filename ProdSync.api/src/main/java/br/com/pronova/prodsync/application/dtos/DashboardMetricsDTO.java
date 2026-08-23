package br.com.pronova.prodsync.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardMetricsDTO {

    private Integer faixaHoraria;
    private Long headcountAtivo;
    private Double totalMovimentacoes;
    private Double mediaProdutividade;

}
