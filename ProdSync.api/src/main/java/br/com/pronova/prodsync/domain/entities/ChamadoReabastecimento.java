package br.com.pronova.prodsync.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;

@Entity
@Table(name = "chamados_reabastecimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class ChamadoReabastecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separador_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Colaborador separador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empilhadeirista_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Colaborador empilhadeirista;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Version
    @Column(nullable = false)
    private Long version;
}
