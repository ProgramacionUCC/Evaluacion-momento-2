CREATE DATABASE IF NOT EXISTS sistema_gestion_usuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sistema_gestion_usuarios;

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    tipo_usuario ENUM('ADMINISTRADOR', 'EMPLEADO', 'CLIENTE') NOT NULL,

    nivel_acceso INT NULL,
    cargo VARCHAR(100) NULL,
    salario DECIMAL(10, 2) NULL,
    puntos_fidelidad INT NULL,

    CONSTRAINT chk_nivel_acceso
        CHECK (nivel_acceso IS NULL OR nivel_acceso BETWEEN 1 AND 5),
    CONSTRAINT chk_salario
        CHECK (salario IS NULL OR salario > 0),
    CONSTRAINT chk_puntos_fidelidad
        CHECK (puntos_fidelidad IS NULL OR puntos_fidelidad >= 0)
);

CREATE TABLE solicitudes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT NOT NULL,
    estado ENUM('PENDIENTE', 'EN_PROCESO', 'RESUELTA', 'CANCELADA')
        NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    cliente_id BIGINT NOT NULL,
    empleado_id BIGINT NULL,

    CONSTRAINT fk_solicitud_cliente
        FOREIGN KEY (cliente_id) REFERENCES usuarios(id),
    CONSTRAINT fk_solicitud_empleado
        FOREIGN KEY (empleado_id) REFERENCES usuarios(id)
);