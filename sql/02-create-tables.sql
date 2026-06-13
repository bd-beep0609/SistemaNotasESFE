USE sistema_notas;
GO

-- Tabla usuarios
CREATE TABLE usuarios (
                          id INT IDENTITY(1,1) PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          activo BIT DEFAULT 1,
                          rol VARCHAR(20) DEFAULT 'USUARIO'
);

-- Tabla carreras
CREATE TABLE carreras (
                          id INT IDENTITY(1,1) PRIMARY KEY,
                          nombre VARCHAR(50) UNIQUE NOT NULL,
                          activo BIT DEFAULT 1
);

-- Tabla niveles
CREATE TABLE niveles (
                         id INT IDENTITY(1,1) PRIMARY KEY,
                         nombre VARCHAR(30) NOT NULL,
                         orden INT NOT NULL,
                         activo BIT DEFAULT 1
);

-- Tabla grupos
CREATE TABLE grupos (
                        id INT IDENTITY(1,1) PRIMARY KEY,
                        numero INT NOT NULL,
                        activo BIT DEFAULT 1
);

-- Tabla modulos
CREATE TABLE modulos (
                         id INT IDENTITY(1,1) PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         carrera_id INT NOT NULL,
                         nivel_id INT NOT NULL,
                         grupo_id INT NOT NULL
);

-- Tabla estudiantes
CREATE TABLE estudiantes (
                             id INT IDENTITY(1,1) PRIMARY KEY,
                             carnet VARCHAR(20) UNIQUE NOT NULL,
                             nombre VARCHAR(100) NOT NULL,
                             apellido VARCHAR(100) NOT NULL,
                             carrera_id INT NOT NULL,
                             nivel_id INT NOT NULL,
                             grupo_id INT NOT NULL,
                             activo BIT DEFAULT 1
);

-- Tabla notas
CREATE TABLE notas (
                       id INT IDENTITY(1,1) PRIMARY KEY,
                       estudiante_id INT NOT NULL,
                       modulo_id INT NOT NULL,
                       valor DECIMAL(3,1) NOT NULL,
                       fecha DATE DEFAULT GETDATE(),
                       observacion VARCHAR(255)
);

-- Agregar llaves foráneas
ALTER TABLE modulos ADD CONSTRAINT FK_modulos_carreras FOREIGN KEY (carrera_id) REFERENCES carreras(id);
ALTER TABLE modulos ADD CONSTRAINT FK_modulos_niveles FOREIGN KEY (nivel_id) REFERENCES niveles(id);
ALTER TABLE modulos ADD CONSTRAINT FK_modulos_grupos FOREIGN KEY (grupo_id) REFERENCES grupos(id);
ALTER TABLE estudiantes ADD CONSTRAINT FK_estudiantes_carreras FOREIGN KEY (carrera_id) REFERENCES carreras(id);
ALTER TABLE estudiantes ADD CONSTRAINT FK_estudiantes_niveles FOREIGN KEY (nivel_id) REFERENCES niveles(id);
ALTER TABLE estudiantes ADD CONSTRAINT FK_estudiantes_grupos FOREIGN KEY (grupo_id) REFERENCES grupos(id);
ALTER TABLE notas ADD CONSTRAINT FK_notas_estudiantes FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE;
ALTER TABLE notas ADD CONSTRAINT FK_notas_modulos FOREIGN KEY (modulo_id) REFERENCES modulos(id);