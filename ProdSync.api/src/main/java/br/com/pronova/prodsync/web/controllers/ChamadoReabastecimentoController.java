package br.com.pronova.prodsync.web.controllers;

import br.com.pronova.prodsync.application.services.ChamadoReabastecimentoService;
import br.com.pronova.prodsync.domain.entities.ChamadoReabastecimento;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chamados")
@RequiredArgsConstructor
public class ChamadoReabastecimentoController {

    private final ChamadoReabastecimentoService chamadoService;

    @PostMapping
    public ResponseEntity<ChamadoReabastecimento> criarChamado(@RequestBody CriarChamadoRequest request) {
        ChamadoReabastecimento chamado = chamadoService.criarChamado(request.getSeparadorId(), request.getEndereco());
        return ResponseEntity.ok(chamado);
    }

    @PutMapping("/{id}/aceitar")
    public ResponseEntity<ChamadoReabastecimento> aceitarChamado(
            @PathVariable Long id, 
            @RequestBody AceitarChamadoRequest request) {
        ChamadoReabastecimento chamado = chamadoService.aceitarChamado(id, request.getEmpilhadeiristaId());
        return ResponseEntity.ok(chamado);
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<ChamadoReabastecimento> concluirChamado(@PathVariable Long id) {
        ChamadoReabastecimento chamado = chamadoService.concluirChamado(id);
        return ResponseEntity.ok(chamado);
    }

    @Data
    public static class CriarChamadoRequest {
        private Long separadorId;
        private String endereco;
    }

    @Data
    public static class AceitarChamadoRequest {
        private Long empilhadeiristaId;
    }
}
