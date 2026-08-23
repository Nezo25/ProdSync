package br.com.pronova.prodsync.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipos_atividade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAtividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "unidade_medida", nullable = false, length = 50)
    private String unidadeMedida;

}
