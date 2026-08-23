package br.com.pronova.prodsync.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebhookPayloadDTO {

    @NotBlank(message = "O nome do colaborador é obrigatório")
    private String nomeColaborador;

    @NotBlank(message = "O tipo de atividade é obrigatório")
    private String tipoAtividade;

    @NotNull(message = "A quantidade primária é obrigatória")
    @Positive(message = "A quantidade primária deve ser maior que zero")
    private Double quantidadePrimaria;

    private Double quantidadeSecundaria;

    @NotNull(message = "A data e hora do registro são obrigatórias")
    private LocalDateTime dataHora;
}
