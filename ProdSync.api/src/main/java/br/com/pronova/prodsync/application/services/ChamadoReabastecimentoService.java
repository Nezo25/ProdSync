package br.com.pronova.prodsync.application.services;

import br.com.pronova.prodsync.domain.entities.ChamadoReabastecimento;
import br.com.pronova.prodsync.domain.entities.Colaborador;
import br.com.pronova.prodsync.domain.repositories.ChamadoReabastecimentoRepository;
import br.com.pronova.prodsync.domain.repositories.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChamadoReabastecimentoService {

    private final ChamadoReabastecimentoRepository chamadoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ChamadoReabastecimento criarChamado(Long separadorId, String endereco) {
        Colaborador separador = colaboradorRepository.findById(separadorId)
                .orElseThrow(() -> new IllegalArgumentException("Separador não encontrado"));

        ChamadoReabastecimento chamado = ChamadoReabastecimento.builder()
                .separador(separador)
                .endereco(endereco)
                .status("PENDENTE")
                .build();

        ChamadoReabastecimento salvo = chamadoRepository.save(chamado);

        // Notifica TODAS as empilhadeiras (Tópico Global)
        messagingTemplate.convertAndSend("/topic/chamados", salvo);

        return salvo;
    }

    @Transactional
    public ChamadoReabastecimento aceitarChamado(Long chamadoId, Long empilhadeiristaId) {
        ChamadoReabastecimento chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        if (!"PENDENTE".equals(chamado.getStatus())) {
            throw new IllegalStateException("O chamado já não está mais pendente.");
        }

        Colaborador empilhadeirista = colaboradorRepository.findById(empilhadeiristaId)
                .orElseThrow(() -> new IllegalArgumentException("Empilhadeirista não encontrado"));

        chamado.setEmpilhadeirista(empilhadeirista);
        chamado.setStatus("EM_DESLOCAMENTO");

        // O Hibernate fará o check do @Version na hora do flush/commit.
        // Se outro empilhadeirista tiver pego, vai lançar ObjectOptimisticLockingFailureException
        ChamadoReabastecimento salvo = chamadoRepository.save(chamado);

        // Notifica de forma direcionada apenas o separador que abriu o chamado
        messagingTemplate.convertAndSend("/topic/chamados/" + chamado.getSeparador().getId(), salvo);
        
        // Notifica o canal global para remover da lista das outras empilhadeiras
        messagingTemplate.convertAndSend("/topic/chamados", salvo);

        return salvo;
    }

    @Transactional
    public ChamadoReabastecimento concluirChamado(Long chamadoId) {
        ChamadoReabastecimento chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));

        chamado.setStatus("CONCLUIDO");
        ChamadoReabastecimento salvo = chamadoRepository.save(chamado);

        // Notifica o separador de que a empilhadeira chegou e concluiu
        messagingTemplate.convertAndSend("/topic/chamados/" + chamado.getSeparador().getId(), salvo);

        return salvo;
    }
}
