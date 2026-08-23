ALTER TABLE colaboradores ADD COLUMN role VARCHAR(50) DEFAULT 'ROLE_SEPARADOR';

-- Update existing records to logical roles
UPDATE colaboradores SET role = 'ROLE_SEPARADOR' WHERE id = 1;
UPDATE colaboradores SET role = 'ROLE_EMPILHADEIRA' WHERE id = 2;
UPDATE colaboradores SET role = 'ROLE_INVENTARIO' WHERE id = 3;
