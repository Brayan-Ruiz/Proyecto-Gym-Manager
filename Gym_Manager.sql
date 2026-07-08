-- =====================================================================
-- Gym Manager
-- Universidad Fidélitas - SC-403 Desarrollo de Aplicaciones Web y Patrones
-- Autor: Brayan Ruiz Valverde
-- =====================================================================

-- Creación de la base de datos y usuario

CREATE DATABASE IF NOT EXISTS gym_manager
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gym_manager;

-- Usuario  (Usuario / Credenciales - HU-01, HU-02, HU-04)

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario      INT AUTO_INCREMENT PRIMARY KEY,
    correo          VARCHAR(100)  NOT NULL UNIQUE,
    contrasena      VARCHAR(255)  NOT NULL,
    rol             ENUM('ADMIN', 'INSTRUCTOR', 'MIEMBRO') NOT NULL,
    activo          BOOLEAN       NOT NULL DEFAULT TRUE,
    fecha_creacion  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Miembro  (HU-05, HU-07, HU-18)

CREATE TABLE IF NOT EXISTS miembro (
    id_miembro          INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario          INT UNIQUE,
    nombre              VARCHAR(60)  NOT NULL,
    primer_apellido     VARCHAR(60)  NOT NULL,
    segundo_apellido    VARCHAR(60),
    sexo                ENUM('MASCULINO', 'FEMENINO', 'OTRO'),
    peso                DECIMAL(5,2),
    estatura            DECIMAL(4,2),
    correo              VARCHAR(100) NOT NULL UNIQUE,
    estado              ENUM('ACTIVO', 'BLOQUEADO') NOT NULL DEFAULT 'ACTIVO',
    fecha_registro      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_miembro_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- Instructor  (HU-06, HU-07, HU-19)

CREATE TABLE IF NOT EXISTS instructor (
    id_instructor       INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario          INT UNIQUE,
    nombre              VARCHAR(60)  NOT NULL,
    primer_apellido     VARCHAR(60)  NOT NULL,
    segundo_apellido    VARCHAR(60),
    edad                INT,
    correo              VARCHAR(100) NOT NULL UNIQUE,
    cedula              VARCHAR(20)  NOT NULL UNIQUE,
    numero_contrato     VARCHAR(20)  NOT NULL UNIQUE,
    estado              ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    fecha_registro      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_instructor_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- Inventario  (HU-08, HU-14)

CREATE TABLE IF NOT EXISTS inventario (
    id_inventario           INT AUTO_INCREMENT PRIMARY KEY,
    nombre_articulo         VARCHAR(100) NOT NULL,
    categoria               ENUM('MAQUINA', 'INDUMENTARIA') NOT NULL,
    cantidad                INT NOT NULL DEFAULT 0,
    estado_conservacion     ENUM('NUEVO', 'BUENO', 'REGULAR', 'DANADO') NOT NULL DEFAULT 'BUENO',
    descripcion             VARCHAR(255)
) ENGINE=InnoDB;


-- Rutina  (HU-10, HU-11, HU-12)

CREATE TABLE IF NOT EXISTS rutina (
    id_rutina           INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    descripcion         VARCHAR(255),
    fecha_creacion      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_instructor       INT NOT NULL,
    id_miembro          INT NOT NULL,
    CONSTRAINT fk_rutina_instructor
        FOREIGN KEY (id_instructor) REFERENCES instructor(id_instructor)
        ON DELETE CASCADE,
    CONSTRAINT fk_rutina_miembro
        FOREIGN KEY (id_miembro) REFERENCES miembro(id_miembro)
        ON DELETE CASCADE
) ENGINE=InnoDB;


-- Ejercicio_rutina

CREATE TABLE IF NOT EXISTS ejercicio_rutina (
    id_ejercicio        INT AUTO_INCREMENT PRIMARY KEY,
    nombre_ejercicio     VARCHAR(100) NOT NULL,
    series               INT NOT NULL,
    repeticiones         INT NOT NULL,
    peso_sugerido        DECIMAL(6,2),
    observaciones        VARCHAR(255),
    id_rutina            INT NOT NULL,
    CONSTRAINT fk_ejercicio_rutina
        FOREIGN KEY (id_rutina) REFERENCES rutina(id_rutina)
        ON DELETE CASCADE
) ENGINE=InnoDB;


-- Historial_pago 

CREATE TABLE IF NOT EXISTS historial_pago (
    id_pago         INT AUTO_INCREMENT PRIMARY KEY,
    id_miembro      INT NOT NULL,
    mes             TINYINT NOT NULL CHECK (mes BETWEEN 1 AND 12),
    anio            SMALLINT NOT NULL,
    fecha_pago      DATE,
    monto           DECIMAL(8,2) NOT NULL,
    estado          ENUM('PAGADO', 'PENDIENTE') NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_pago_miembro
        FOREIGN KEY (id_miembro) REFERENCES miembro(id_miembro)
        ON DELETE CASCADE,
    CONSTRAINT uq_pago_miembro_mes UNIQUE (id_miembro, mes, anio)
) ENGINE=InnoDB;


-- Sesion_acceso

CREATE TABLE IF NOT EXISTS sesion_acceso (
    id_sesion       INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT NOT NULL,
    fecha_hora      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resultado       ENUM('EXITOSO', 'DENEGADO') NOT NULL,
    motivo_bloqueo  VARCHAR(150),
    CONSTRAINT fk_sesion_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
) ENGINE=InnoDB;


-- Índices

CREATE INDEX idx_rutina_miembro ON rutina(id_miembro);
CREATE INDEX idx_rutina_instructor ON rutina(id_instructor);
CREATE INDEX idx_pago_miembro ON historial_pago(id_miembro);
CREATE INDEX idx_pago_estado ON historial_pago(estado);


-- Datos de prueba 

-- Administrador de prueba
INSERT INTO usuario (correo, contrasena, rol, activo)
VALUES ('admin@gymmanager.com', 'admin123', 'ADMIN', TRUE);

-- Instructor de prueba
INSERT INTO usuario (correo, contrasena, rol, activo)
VALUES ('instructor1@gymmanager.com', 'instructor123', 'INSTRUCTOR', TRUE);

INSERT INTO instructor (id_usuario, nombre, primer_apellido, segundo_apellido, edad, correo, cedula, numero_contrato, estado)
VALUES (
    (SELECT id_usuario FROM usuario WHERE correo = 'instructor1@gymmanager.com'),
    'Carlos', 'Jiménez', 'González', 29, 'instructor1@gymmanager.com', '1-1111-1111', 'CT-001', 'ACTIVO'
);

-- Miembro de prueba
INSERT INTO usuario (correo, contrasena, rol, activo)
VALUES ('miembro1@gymmanager.com', 'miembro123', 'MIEMBRO', TRUE);

INSERT INTO miembro (id_usuario, nombre, primer_apellido, segundo_apellido, sexo, peso, estatura, correo, estado)
VALUES (
    (SELECT id_usuario FROM usuario WHERE correo = 'miembro1@gymmanager.com'),
    'Gabriel', 'Cordero', 'Quesada', 'MASCULINO', 78.50, 1.75, 'miembro1@gymmanager.com', 'ACTIVO'
);

-- Pago pendiente de ejemplo para el miembro de prueba (para probar HU-03 y HU-15)
INSERT INTO historial_pago (id_miembro, mes, anio, fecha_pago, monto, estado)
VALUES (
    (SELECT id_miembro FROM miembro WHERE correo = 'miembro1@gymmanager.com'),
    MONTH(CURDATE()), YEAR(CURDATE()), NULL, 35000.00, 'PENDIENTE'
);

-- Usuario de Prueba Admin
CREATE USER IF NOT EXISTS 'usuario_gym_manager'@'localhost' IDENTIFIED BY 'gym123';
GRANT ALL PRIVILEGES ON gym_manager.* TO 'usuario_gym_manager'@'localhost';
FLUSH PRIVILEGES;

-- Artículos de inventario de ejemplo
INSERT INTO inventario (nombre_articulo, categoria, cantidad, estado_conservacion, descripcion)
VALUES
    ('Caminadora', 'MAQUINA', 4, 'BUENO', 'Caminadoras ubicadas en el área de cardio'),
    ('Set de Mancuernas 5-25kg', 'MAQUINA', 10, 'NUEVO', 'Set completo de mancuernas de hierro fundido'),
    ('Colchonetas de Yoga', 'INDUMENTARIA', 20, 'BUENO', 'Colchonetas antideslizantes para clases grupales');