package br.com.pronova.prodsync.web.controllers;

import br.com.pronova.prodsync.application.dtos.WebhookPayloadDTO;
import br.com.pronova.prodsync.application.services.WebhookProtheusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhooks/totvs")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookProtheusService webhookService;

    @PostMapping("/produtividade")
    public ResponseEntity<Void> receberProdutividade(@Valid @RequestBody WebhookPayloadDTO payload) {
        webhookService.processarWebhook(payload);
        return ResponseEntity.accepted().build();
    }
}
