CREATE TABLE chamado_inventario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    separador_id BIGINT NOT NULL,
    inventariante_id BIGINT,
    endereco VARCHAR(100) NOT NULL,
    sku_produto VARCHAR(50) NOT NULL,
    motivo VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_abertura DATETIME NOT NULL,
    data_atendimento DATETIME,
    data_resolucao DATETIME,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_inventario_separador FOREIGN KEY (separador_id) REFERENCES colaboradores(id),
    CONSTRAINT fk_inventario_inventariante FOREIGN KEY (inventariante_id) REFERENCES colaboradores(id)
);
