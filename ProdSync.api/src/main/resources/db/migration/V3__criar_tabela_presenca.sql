-- 1. Cria a tabela base do Hibernate Envers (se ja nao existir)
CREATE TABLE IF NOT EXISTS REVINFO (
    REV INT AUTO_INCREMENT PRIMARY KEY,
    REVTSTMP BIGINT
);

-- 2. Cria a tabela de Presenca
CREATE TABLE registros_presenca (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    colaborador_id BIGINT NOT NULL,
    data_referencia DATE NOT NULL,
    status_presenca ENUM('PRESENTE', 'FALTA', 'ATESTADO', 'FERIAS') DEFAULT 'PRESENTE',
    hora_entrada TIME DEFAULT '08:00:00',
    hora_saida TIME DEFAULT '17:00:00',
    hora_inicio_pausa TIME DEFAULT '12:00:00',
    hora_fim_pausa TIME DEFAULT '13:00:00',
    observacao VARCHAR(255),
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_presenca_colab FOREIGN KEY (colaborador_id) REFERENCES colaboradores(id)
);

-- 3. Cria a tabela de Auditoria
CREATE TABLE registros_presenca_AUD (
    id BIGINT NOT NULL,
    REV INT NOT NULL,
    REVTYPE TINYINT,
    colaborador_id BIGINT,
    data_referencia DATE,
    status_presenca VARCHAR(50),
    hora_entrada TIME,
    hora_saida TIME,
    hora_inicio_pausa TIME,
    hora_fim_pausa TIME,
    observacao VARCHAR(255),
    PRIMARY KEY (id, REV),
    CONSTRAINT fk_presenca_aud_rev FOREIGN KEY (REV) REFERENCES REVINFO(REV)
);
