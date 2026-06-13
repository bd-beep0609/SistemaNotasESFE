# 📋 Requerimientos del Sistema de Notas ESFE

**Autor:** Bayron  
**Versión:** 1.0  
**Fecha:** 09/06/2025

---

## 🎯 Objetivo

Desarrollar una aplicación de escritorio en Java Swing para gestionar notas de estudiantes de la carrera de Software (Articulado) en ESFE.

---

## 👥 Usuarios

| Rol | Descripción |
|-----|-------------|
| **Administrador** | Gestiona estudiantes, notas, usuarios y reportes |
| **Usuario** | Consulta notas y reportes (futura implementación) |

---

## 🔐 Requerimientos Funcionales

### Módulo 1: Autenticación
| ID | Requerimiento |
|----|---------------|
| RF-01 | Pantalla de login con email y contraseña |
| RF-02 | Checkbox "Mostrar contraseña" para ver u ocultar texto |
| RF-03 | Usuario por defecto: admin@demo.com / admin123 |

### Módulo 2: Gestión de Estudiantes
| ID | Requerimiento |
|----|---------------|
| RF-04 | CRUD completo de estudiantes |
| RF-05 | Campos: Carnet (único), Nombre, Apellido, Carrera, Nivel, Grupo, Estado |
| RF-06 | Tabla con: ID, Carnet, Nombre, Apellido, Curso, Estado |
| RF-07 | Barra de búsqueda en tiempo real |
| RF-08 | Estadísticas: total, activos, inactivos, por carrera |

### Módulo 3: Gestión de Notas
| ID | Requerimiento |
|----|---------------|
| RF-09 | Registrar notas (0-10) por estudiante y módulo |
| RF-10 | Observación opcional |
| RF-11 | Tabla de notas registradas |
| RF-12 | Fechas formateadas (dd/MM/yyyy) |

### Módulo 4: Reportes
| ID | Requerimiento |
|----|---------------|
| RF-13 | Promedio por estudiante (aprobado ≥ 6.0) |
| RF-14 | Promedio por carrera |

### Módulo 5: Seguridad y Persistencia
| ID | Requerimiento |
|----|---------------|
| RF-15 | Base de datos SQL Server |
| RF-16 | Creación automática de BD y tablas |
| RF-17 | Cambio de contraseña |

---

## 🛠️ Requerimientos Técnicos

| Requisito | Especificación |
|-----------|----------------|
| Lenguaje | Java 21 |
| GUI | Swing |
| Base de Datos | SQL Server |
| Driver JDBC | mssql-jdbc-12.8.2.jre11 |
| Control de Versiones | Git / GitHub |
| Metodología | MVC (model, view, controller) |

---

## ✅ Criterios de Aceptación

- [x] Login funciona con admin@demo.com / admin123
- [x] Se puede ver/ocultar contraseña
- [x] CRUD completo de estudiantes
- [x] Búsqueda en tiempo real
- [x] Registro de notas (0-10)
- [x] Reportes de promedios
- [x] Persistencia en SQL Server
- [x] Código comentado y organizado