ALTER TABLE registros_produtividade RENAME COLUMN quantidade TO quantidade_primaria;
ALTER TABLE registros_produtividade ADD COLUMN quantidade_secundaria DOUBLE;

-- Update the audit table as well
ALTER TABLE registros_produtividade_aud RENAME COLUMN quantidade TO quantidade_primaria;
ALTER TABLE registros_produtividade_aud ADD COLUMN quantidade_secundaria DOUBLE;
