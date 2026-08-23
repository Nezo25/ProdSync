package br.com.pronova.prodsync.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "chamado_inventario")
@Data
@NoArgsConstructor
public class ChamadoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "separador_id")
    private Colaborador separador;

    @ManyToOne
    @JoinColumn(name = "inventariante_id")
    private Colaborador inventariante;

    @Column(nullable = false, length = 100)
    private String endereco;

    @Column(name = "sku_produto", nullable = false, length = 50)
    private String skuProduto;

    @Column(nullable = false, length = 50)
    private String motivo; // DIVERGENCIA_SALDO, PRODUTO_AVARIADO

    @Column(nullable = false, length = 50)
    private String status; // PENDENTE, EM_ANALISE, RESOLVIDO

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_atendimento")
    private LocalDateTime dataAtendimento;

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        this.dataAbertura = LocalDateTime.now();
        this.status = "PENDENTE";
    }
}
