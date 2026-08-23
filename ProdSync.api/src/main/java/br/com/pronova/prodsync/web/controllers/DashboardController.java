package br.com.pronova.prodsync.web.controllers;

import br.com.pronova.prodsync.application.dtos.DashboardMetricsDTO;
import br.com.pronova.prodsync.application.services.ProdutividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.pronova.prodsync.application.dtos.DashboardMatrizDTO;
import br.com.pronova.prodsync.application.services.DashboardService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ProdutividadeService produtividadeService;
    private final DashboardService dashboardService;

    @GetMapping("/metricas")
    public ResponseEntity<DashboardMetricsDTO> obterMetricas(
            @RequestParam Integer faixaHoraria,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        LocalDateTime dataBusca = data.atStartOfDay();
        return ResponseEntity.ok(produtividadeService.obterMetricasPorFaixaHoraria(faixaHoraria, dataBusca));
    }

    @GetMapping("/matriz-picking")
    public ResponseEntity<List<DashboardMatrizDTO>> getMatrizPicking(
            @RequestParam(value = "data", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        if (data == null) {
            data = LocalDate.now();
        }

        List<DashboardMatrizDTO> matriz = dashboardService.getMatrizPicking(data);
        return ResponseEntity.ok(matriz);
    }
}
