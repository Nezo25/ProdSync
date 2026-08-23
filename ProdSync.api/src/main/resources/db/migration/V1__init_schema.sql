CREATE TABLE turnos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    horario_inicio TIME NOT NULL,
    horario_fim TIME NOT NULL,
    version BIGINT DEFAULT 0
);

CREATE TABLE tipos_atividade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    unidade_medida VARCHAR(50),
    version BIGINT DEFAULT 0
);

CREATE TABLE colaboradores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    turno_id BIGINT,
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_colab_turno FOREIGN KEY (turno_id) REFERENCES turnos(id)
);

CREATE TABLE registros_produtividade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    colaborador_id BIGINT NOT NULL,
    turno_id BIGINT,
    tipo_atividade_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    data_hora DATETIME NOT NULL,
    faixa_horaria INT NOT NULL,
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_prod_colab FOREIGN KEY (colaborador_id) REFERENCES colaboradores(id),
    CONSTRAINT fk_prod_turno FOREIGN KEY (turno_id) REFERENCES turnos(id),
    CONSTRAINT fk_prod_tipo FOREIGN KEY (tipo_atividade_id) REFERENCES tipos_atividade(id)
);


CREATE TABLE REVINFO (
    REV INT AUTO_INCREMENT PRIMARY KEY,
    REVTSTMP BIGINT
);

CREATE TABLE registros_produtividade_aud (
    id BIGINT NOT NULL,
    REV INT NOT NULL,
    REVTYPE TINYINT,
    colaborador_id BIGINT,
    turno_id BIGINT,
    tipo_atividade_id BIGINT,
    quantidade INT,
    data_hora DATETIME,
    faixa_horaria INT,
    PRIMARY KEY (id, REV),
    CONSTRAINT fk_produtividade_revinfo FOREIGN KEY (REV) REFERENCES REVINFO(REV)
);

