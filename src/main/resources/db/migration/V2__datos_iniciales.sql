-- V2: datos de ejemplo para ver algo en pantalla al levantar el proyecto
INSERT INTO tarjeta_bip (numero, titular, alias, tipo, saldo, estado) VALUES
    ('12345678', 'María González',  'Mi Bip del trabajo', 'NORMAL',       5200.00, 'ACTIVA'),
    ('23456789', 'Juan Pérez',      'Universidad',        'TNE_SUPERIOR', 1500.00, 'ACTIVA'),
    ('45678901', 'Rosa Muñoz',      NULL,                 'ADULTO_MAYOR', 3000.00, 'BLOQUEADA');
