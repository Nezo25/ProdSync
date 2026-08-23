CREATE TABLE chamados_reabastecimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    separador_id BIGINT NOT NULL,
    empilhadeirista_id BIGINT,
    endereco VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    version BIGINT NOT NULL,
    CONSTRAINT fk_chamado_separador FOREIGN KEY (separador_id) REFERENCES colaboradores(id),
    CONSTRAINT fk_chamado_empilhadeirista FOREIGN KEY (empilhadeirista_id) REFERENCES colaboradores(id)
);
