USE sistema_notas;
GO

-- Usuario admin
INSERT INTO usuarios (nombre, email, password, activo, rol)
VALUES ('Administrador', 'admin@demo.com', 'admin123', 1, 'ADMIN');

-- Carreras
INSERT INTO carreras (nombre) VALUES ('Software'), ('Turismo'), ('Eléctrica'), ('Mercadeo');

-- Niveles
INSERT INTO niveles (nombre, orden) VALUES ('1°', 1), ('2°', 2), ('3°', 3), ('4° Articulado', 4);

-- Grupos
INSERT INTO grupos (numero) VALUES (1), (2), (3), (4), (5);

-- Módulos Software
INSERT INTO modulos (nombre, carrera_id, nivel_id, grupo_id) VALUES
                                                                 ('Módulo de Software', 1, 1, 2),
                                                                 ('Inglés', 1, 1, 2),
                                                                 ('Módulo de Software', 1, 2, 1),
                                                                 ('Inglés', 1, 2, 1),
                                                                 ('Módulo de Software', 1, 4, 3),
                                                                 ('Inglés', 1, 4, 3);

-- Estudiantes
INSERT INTO estudiantes (carnet, nombre, apellido, carrera_id, nivel_id, grupo_id, activo) VALUES
                                                                                               ('20250001', 'Carlos', 'Pérez', 1, 1, 2, 1),
                                                                                               ('20250002', 'Ana', 'Gómez', 1, 1, 2, 1),
                                                                                               ('20250003', 'Luis', 'Martínez', 1, 2, 1, 1),
                                                                                               ('20250004', 'María', 'López', 1, 4, 3, 1);

-- Notas
INSERT INTO notas (estudiante_id, modulo_id, valor, observacion) VALUES
                                                                     (1, 1, 8.5, 'Examen parcial'),
                                                                     (1, 2, 9.0, 'Tarea final'),
                                                                     (2, 1, 9.5, 'Excelente participación');