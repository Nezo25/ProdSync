package br.com.pronova.prodsync.domain.entities;

import br.com.pronova.prodsync.domain.enums.StatusPresenca;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "registros_presenca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class RegistroPresenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Colaborador colaborador;

    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_presenca")
    @Builder.Default
    private StatusPresenca statusPresenca = StatusPresenca.PRESENTE;

    @Column(name = "hora_entrada")
    @Builder.Default
    private LocalTime horaEntrada = LocalTime.of(8, 0);

    @Column(name = "hora_saida")
    @Builder.Default
    private LocalTime horaSaida = LocalTime.of(17, 0);

    @Column(name = "hora_inicio_pausa")
    @Builder.Default
    private LocalTime horaInicioPausa = LocalTime.of(12, 0);

    @Column(name = "hora_fim_pausa")
    @Builder.Default
    private LocalTime horaFimPausa = LocalTime.of(13, 0);

    @Column(length = 255)
    private String observacao;
}
