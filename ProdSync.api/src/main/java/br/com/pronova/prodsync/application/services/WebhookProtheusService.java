package br.com.pronova.prodsync.application.services;

import br.com.pronova.prodsync.application.dtos.WebhookPayloadDTO;
import br.com.pronova.prodsync.application.mappers.RegistroProdutividadeMapper;
import br.com.pronova.prodsync.domain.entities.Colaborador;
import br.com.pronova.prodsync.domain.entities.RegistroProdutividade;
import br.com.pronova.prodsync.domain.entities.TipoAtividade;
import br.com.pronova.prodsync.domain.repositories.ColaboradorRepository;
import br.com.pronova.prodsync.domain.repositories.RegistroProdutividadeRepository;
import br.com.pronova.prodsync.domain.repositories.TipoAtividadeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookProtheusService {

    private final RegistroProdutividadeRepository registroRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final TipoAtividadeRepository tipoAtividadeRepository;
    private final RegistroProdutividadeMapper mapper;

    @Transactional
    @org.springframework.scheduling.annotation.Async("webhookTaskExecutor")
    @org.springframework.cache.annotation.CacheEvict(value = "metricas_produtividade", allEntries = true)
    public void processarWebhook(WebhookPayloadDTO payload) {
        Colaborador colaborador = colaboradorRepository.findByNome(payload.getNomeColaborador())
                .orElseGet(() -> colaboradorRepository.save(Colaborador.builder().nome(payload.getNomeColaborador()).build()));

        TipoAtividade tipoAtividade = tipoAtividadeRepository.findByNome(payload.getTipoAtividade())
                .orElseGet(() -> tipoAtividadeRepository.save(TipoAtividade.builder().nome(payload.getTipoAtividade()).unidadeMedida("UN").build()));

        RegistroProdutividade registro = mapper.toEntity(payload);
        registro.setColaborador(colaborador);
        registro.setTipoAtividade(tipoAtividade);

        registroRepository.save(registro);
    }
    
    @org.springframework.cache.annotation.CacheEvict(value = "metricas_produtividade", allEntries = true)
    public void evictCache() {
        // Will be called automatically or manually, but actually the simplest is putting @CacheEvict on the save method.
    }
}
