INSERT INTO colaboradores (id, nome, ativo, version) VALUES 
(1, 'CAIQUE RICARDO DE TOLEDO', 1, 0),
(2, 'WESLEY DIAS SANTOS', 1, 0)
ON DUPLICATE KEY UPDATE nome=VALUES(nome);

INSERT INTO turnos (id, descricao, horario_inicio, horario_fim, version) VALUES 
(1, 'Turno Padrão', '08:00:00', '17:00:00', 0),
(2, 'Turno Sexta', '08:00:00', '16:00:00', 0)
ON DUPLICATE KEY UPDATE descricao=VALUES(descricao);

INSERT INTO tipos_atividade (id, nome, unidade_medida, version) VALUES 
(1, 'Separação', 'UN', 0)
ON DUPLICATE KEY UPDATE nome=VALUES(nome);

-- Inserindo logs para a data de HOJE (substitua a data se necessário, mantendo o horário)
-- 08:00
INSERT INTO registros_produtividade (colaborador_id, turno_id, tipo_atividade_id, quantidade_primaria, quantidade_secundaria, data_hora, faixa_horaria, version) 
VALUES (1, 1, 1, 350, 32, CURRENT_DATE() + INTERVAL 8 HOUR, 8, 0);

INSERT INTO registros_produtividade (colaborador_id, turno_id, tipo_atividade_id, quantidade_primaria, quantidade_secundaria, data_hora, faixa_horaria, version) 
VALUES (2, 1, 1, 280, 25, CURRENT_DATE() + INTERVAL 8 HOUR, 8, 0);

-- 09:00
INSERT INTO registros_produtividade (colaborador_id, turno_id, tipo_atividade_id, quantidade_primaria, quantidade_secundaria, data_hora, faixa_horaria, version) 
VALUES (1, 1, 1, 375, 33, CURRENT_DATE() + INTERVAL 9 HOUR, 9, 0);

INSERT INTO registros_produtividade (colaborador_id, turno_id, tipo_atividade_id, quantidade_primaria, quantidade_secundaria, data_hora, faixa_horaria, version) 
VALUES (2, 1, 1, 310, 27, CURRENT_DATE() + INTERVAL 9 HOUR, 9, 0);
