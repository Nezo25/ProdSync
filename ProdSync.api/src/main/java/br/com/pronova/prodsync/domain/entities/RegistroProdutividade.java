package br.com.pronova.prodsync.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;

@Entity
@Table(name = "registros_produtividade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class RegistroProdutividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Colaborador colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Turno turno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_atividade_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private TipoAtividade tipoAtividade;

    @Column(name = "quantidade_primaria", nullable = false)
    private Double quantidadePrimaria;

    @Column(name = "quantidade_secundaria")
    private Double quantidadeSecundaria;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "faixa_horaria", nullable = false)
    private Integer faixaHoraria; // Ex: 8 para 08:00 - 08:59

}

