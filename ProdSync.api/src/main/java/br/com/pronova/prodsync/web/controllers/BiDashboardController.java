package br.com.pronova.prodsync.web.controllers;

import br.com.pronova.prodsync.application.dtos.CustoOperacaoDTO;
import br.com.pronova.prodsync.application.services.BiDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bi")
@RequiredArgsConstructor
public class BiDashboardController {

    private final BiDashboardService biDashboardService;

    @GetMapping("/financeiro/mensal")
    public ResponseEntity<CustoOperacaoDTO> getCustoOperacaoMensal(
            @RequestParam("ano") Integer ano,
            @RequestParam("mes") Integer mes) {
        
        CustoOperacaoDTO relatorio = biDashboardService.getCustoOperacaoMensal(ano, mes);
        return ResponseEntity.ok(relatorio);
    }
    
    @GetMapping("/colaboradores/{id}/dossie")
    public org.springframework.http.ResponseEntity<br.com.pronova.prodsync.application.dtos.DossieColaboradorDTO> getDossie(
            @PathVariable Long id,
            @RequestParam Integer ano,
            @RequestParam Integer mes) {
        return org.springframework.http.ResponseEntity.ok(biDashboardService.getDossieColaborador(id, ano, mes));
    }

}
