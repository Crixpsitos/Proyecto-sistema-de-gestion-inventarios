-- extension "pg_trgm"
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- =============================================================
-- Datos de prueba - Gestion Inventarios
-- Contraseñas hasheadas con BCryptPasswordEncoder (Spring Security)
--   ADMIN    -> admin123
--   MANAGER  -> manager123
--   EMPLOYEE -> employee123
-- =============================================================

-- -------------------------------------------------------------
-- 1. PERMISOS
-- -------------------------------------------------------------
INSERT INTO permissions (name) VALUES
    ('PRODUCT_READ'),
    ('PRODUCT_CREATE'),
    ('PRODUCT_UPDATE'),
    ('PRODUCT_DELETE'),
    ('INVENTORY_READ'),
    ('INVENTORY_UPDATE'),
    ('USER_READ'),
    ('USER_CREATE'),
    ('USER_UPDATE'),
    ('USER_DELETE'),
    ('REPORT_READ')
ON CONFLICT (name) DO NOTHING;

-- -------------------------------------------------------------
-- 2. ROLES
-- -------------------------------------------------------------
INSERT INTO roles (name) VALUES
    ('ADMIN'),
    ('MANAGER'),
    ('EMPLOYEE')
ON CONFLICT (name) DO NOTHING;

-- -------------------------------------------------------------
-- 3. PERMISOS POR ROL
-- -------------------------------------------------------------

-- ADMIN: acceso total
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

-- MANAGER: todo menos eliminar usuarios
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'PRODUCT_READ', 'PRODUCT_CREATE', 'PRODUCT_UPDATE', 'PRODUCT_DELETE',
    'INVENTORY_READ', 'INVENTORY_UPDATE',
    'USER_READ', 'USER_CREATE', 'USER_UPDATE',
    'REPORT_READ'
)
WHERE r.name = 'MANAGER'
ON CONFLICT DO NOTHING;

-- EMPLOYEE: solo lectura y actualización de inventario
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'PRODUCT_READ',
    'INVENTORY_READ', 'INVENTORY_UPDATE'
)
WHERE r.name = 'EMPLOYEE'
ON CONFLICT DO NOTHING;

-- -------------------------------------------------------------
-- 4. USUARIOS
-- -------------------------------------------------------------
INSERT INTO "users" (name, last_name, email, password_hash, phone, document_type, document_number, enabled, role_id)
SELECT
    'Carlos', 'Admin', 'admin@inventarios.com',
    '$2a$10$4iwuRK4RgwOILBzKLJV8XuI79twRQ5BeVdL7E6CPCjpo2ZgzHjGRq',
    '+57 300 000 0001',
    'CC', '1000000001', true,
    r.id
FROM roles r WHERE r.name = 'ADMIN'
ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (name, last_name, email, password_hash, phone, document_type, document_number, enabled, role_id)
SELECT
    'María', 'Manager', 'manager@inventarios.com',
    '$2a$10$S0/iB0NgZF/qtNkdM3QfWeIDwaVxOmsAcQRggJe7d8el3RuaihkyO',
    '+57 300 000 0002',
    'CC', '1000000002', true,
    r.id
FROM roles r WHERE r.name = 'MANAGER'
ON CONFLICT (email) DO NOTHING;

INSERT INTO "users" (name, last_name, email, password_hash, phone, document_type, document_number, enabled, role_id)
SELECT
    'Juan', 'Employee', 'employee@inventarios.com',
    '$2a$10$Sg8nFGwCFHAeZ4MnMP.83e7Wrf4pNgQZRh/4jUtJxhSx05HlmmZ4y',
    '+57 300 000 0003',
    'CE', '2000000001', true,
    r.id
FROM roles r WHERE r.name = 'EMPLOYEE'
ON CONFLICT (email) DO NOTHING;

-- -------------------------------------------------------------
-- 5. PERMISO EXTRA (override) para el EMPLOYEE
--    Le damos REPORT_READ como permiso adicional individual
-- -------------------------------------------------------------
INSERT INTO user_permissions (user_id, permission_id, type, assigned_at)
SELECT u.id, p.id, 'override', NOW()
FROM "users" u, permissions p
WHERE u.email = 'employee@inventarios.com'
  AND p.name = 'REPORT_READ'
ON CONFLICT DO NOTHING;

-- -------------------------------------------------------------
-- 6. PERMISO DENEGADO (deny) para el MANAGER
--    Le quitamos USER_UPDATE individualmente
-- -------------------------------------------------------------
INSERT INTO user_permissions (user_id, permission_id, type, assigned_at)
SELECT u.id, p.id, 'deny', NOW()
FROM "users" u, permissions p
WHERE u.email = 'manager@inventarios.com'
  AND p.name = 'USER_UPDATE'
ON CONFLICT DO NOTHING;

-- -------------------------------------------------------------
-- 7. CATEGORÍAS
-- -------------------------------------------------------------
INSERT INTO categories (name, description, created_at, updated_at) VALUES
('Electrónica',       'Dispositivos electrónicos y accesorios',         NOW(), NOW()),
('Ropa y Accesorios', 'Prendas de vestir, zapatos y complementos',       NOW(), NOW()),
('Alimentos',         'Productos alimenticios y bebidas',                NOW(), NOW()),
('Hogar y Muebles',   'Artículos para el hogar y mobiliario',            NOW(), NOW()),
('Herramientas',      'Herramientas manuales y eléctricas',              NOW(), NOW()),
('Deportes',          'Equipamiento deportivo y ropa deportiva',         NOW(), NOW()),
('Juguetes',          'Juguetes y juegos para todas las edades',         NOW(), NOW()),
('Salud y Belleza',   'Productos de cuidado personal y salud',           NOW(), NOW()),
('Papelería',         'Útiles escolares y de oficina',                   NOW(), NOW()),
('Automotriz',        'Repuestos, accesorios y cuidado del vehículo',    NOW(), NOW())
ON CONFLICT DO NOTHING;


CREATE INDEX idx_categories_search ON categories USING gin ((name || ' ' || description) gin_trgm_ops);