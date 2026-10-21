-- V1: tabla principal del CRUD
CREATE TABLE tarjeta_bip (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero         VARCHAR(10)   NOT NULL,
    titular        VARCHAR(100)  NOT NULL,
    alias          VARCHAR(50),
    tipo           VARCHAR(20)   NOT NULL,
    saldo          NUMERIC(10,2) NOT NULL DEFAULT 0,
    estado         VARCHAR(10)   NOT NULL DEFAULT 'ACTIVA',
    creada_en      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizada_en TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_tarjeta_bip_numero UNIQUE (numero),
    CONSTRAINT ck_tarjeta_bip_numero CHECK (numero ~ '^[0-9]{8,10}$'),
    CONSTRAINT ck_tarjeta_bip_tipo   CHECK (tipo IN ('NORMAL', 'TNE_ESCOLAR', 'TNE_SUPERIOR', 'ADULTO_MAYOR')),
    CONSTRAINT ck_tarjeta_bip_estado CHECK (estado IN ('ACTIVA', 'BLOQUEADA')),
    CONSTRAINT ck_tarjeta_bip_saldo  CHECK (saldo >= 0 AND saldo <= 100000)
);

CREATE INDEX idx_tarjeta_bip_titular ON tarjeta_bip (titular);
