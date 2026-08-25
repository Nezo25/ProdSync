DELETE c1 FROM colaboradores c1
INNER JOIN colaboradores c2 
WHERE c1.id > c2.id AND c1.nome = c2.nome;

DELETE t1 FROM tipos_atividade t1
INNER JOIN tipos_atividade t2
WHERE t1.id > t2.id AND t1.nome = t2.nome;
