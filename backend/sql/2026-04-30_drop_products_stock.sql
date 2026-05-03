-- Quita la columna legacy stock de products.
-- Ejecutar en PostgreSQL dentro de una transaccion de mantenimiento.

ALTER TABLE products
DROP COLUMN IF EXISTS stock;
