package br.com.pronova.prodsync.web.controllers;

import br.com.pronova.prodsync.application.services.CsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/importacao")
@RequiredArgsConstructor
public class ImportacaoController {

    private final CsvImportService csvImportService;

    @PostMapping("/csv")
    public ResponseEntity<String> importarCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo não pode estar vazio");
        }
        
        csvImportService.processarCsv(file);
        return ResponseEntity.ok("Arquivo processado com sucesso!");
    }
}
