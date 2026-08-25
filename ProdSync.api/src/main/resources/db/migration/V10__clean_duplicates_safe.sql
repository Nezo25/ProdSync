UPDATE registros_produtividade rp
JOIN colaboradores c2 ON rp.colaborador_id = c2.id
JOIN colaboradores c1 ON c1.nome = c2.nome AND c1.id < c2.id
SET rp.colaborador_id = c1.id;

DELETE c2 FROM colaboradores c2
INNER JOIN colaboradores c1 ON c1.nome = c2.nome AND c1.id < c2.id;

UPDATE registros_produtividade rp
JOIN tipos_atividade t2 ON rp.tipo_atividade_id = t2.id
JOIN tipos_atividade t1 ON t1.nome = t2.nome AND t1.id < t2.id
SET rp.tipo_atividade_id = t1.id;

DELETE t2 FROM tipos_atividade t2
INNER JOIN tipos_atividade t1 ON t1.nome = t2.nome AND t1.id < t2.id;
