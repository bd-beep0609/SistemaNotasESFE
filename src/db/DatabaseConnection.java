package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // Configuración de SQL Server - CAMBIA SEGÚN TU PC
    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "sistema_notas";
    private static final String USER = "sa";
    private static final String PASSWORD = "YourStrong!Passw0rd";  // ⚠️ CAMBIA A TU CONTRASEÑA

    private static final String URL = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
            SERVER, PORT, DATABASE
    );

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conectado a SQL Server - BD: " + DATABASE);
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver SQL Server no encontrado");
                throw new SQLException(e);
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("🔌 Conexión cerrada");
        }
    }

    // Inicializar base de datos (crear BD y tablas si no existen)
    public static void initDatabase() {
        System.out.println("📦 Inicializando base de datos...");

        // Conectar a master para crear la BD
        String masterUrl = String.format(
                "jdbc:sqlserver://%s:%s;encrypt=true;trustServerCertificate=true",
                SERVER, PORT
        );

        try (Connection conn = DriverManager.getConnection(masterUrl, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Crear base de datos si no existe
            stmt.execute("IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = '" + DATABASE + "') " +
                    "CREATE DATABASE " + DATABASE);
            System.out.println("✅ Base de datos '" + DATABASE + "' creada/verificada");

        } catch (SQLException e) {
            System.err.println("❌ Error creando BD: " + e.getMessage());
        }

        // Ahora crear las tablas
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Tabla usuarios
            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='usuarios' AND xtype='U')
                CREATE TABLE usuarios (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    activo BIT DEFAULT 1,
                    rol VARCHAR(20) DEFAULT 'USUARIO'
                )
            """);

            // Tabla carreras
            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='carreras' AND xtype='U')
                CREATE TABLE carreras (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    nombre VARCHAR(50) UNIQUE NOT NULL,
                    activo BIT DEFAULT 1
                )
            """);

            // Tabla niveles
            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='niveles' AND xtype='U')
                CREATE TABLE niveles (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    nombre VARCHAR(30) NOT NULL,
                    orden INT NOT NULL,
                    activo BIT DEFAULT 1
                )
            """);

            // Tabla grupos
            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='grupos' AND xtype='U')
                CREATE TABLE grupos (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    numero INT NOT NULL,
                    activo BIT DEFAULT 1
                )
            """);

            // Tabla modulos
            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='modulos' AND xtype='U')
                CREATE TABLE modulos (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    carrera_id INT NOT NULL,
                    nivel_id INT NOT NULL,
                    grupo_id INT NOT NULL
                )
            """);

            // Tabla estudiantes
            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='estudiantes' AND xtype='U')
                CREATE TABLE estudiantes (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    carnet VARCHAR(20) UNIQUE NOT NULL,
                    nombre VARCHAR(100) NOT NULL,
                    apellido VARCHAR(100) NOT NULL,
                    carrera_id INT NOT NULL,
                    nivel_id INT NOT NULL,
                    grupo_id INT NOT NULL,
                    activo BIT DEFAULT 1
                )
            """);

            // Tabla notas
            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='notas' AND xtype='U')
                CREATE TABLE notas (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    estudiante_id INT NOT NULL,
                    modulo_id INT NOT NULL,
                    valor DECIMAL(3,1) NOT NULL,
                    fecha DATE DEFAULT GETDATE(),
                    observacion VARCHAR(255)
                )
            """);

            System.out.println("✅ Tablas creadas/verificadas");

            // Insertar usuario admin por defecto
            stmt.execute("""
                IF NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@demo.com')
                INSERT INTO usuarios (nombre, email, password, activo, rol) 
                VALUES ('Administrador', 'admin@demo.com', 'admin123', 1, 'ADMIN')
            """);

            // Insertar carreras
            stmt.execute("""
                IF NOT EXISTS (SELECT 1 FROM carreras)
                INSERT INTO carreras (nombre) VALUES ('Software'), ('Turismo'), ('Eléctrica'), ('Mercadeo')
            """);

            // Insertar niveles
            stmt.execute("""
                IF NOT EXISTS (SELECT 1 FROM niveles)
                INSERT INTO niveles (nombre, orden) VALUES ('1°', 1), ('2°', 2), ('3°', 3), ('4° Articulado', 4)
            """);

            // Insertar grupos
            stmt.execute("""
                IF NOT EXISTS (SELECT 1 FROM grupos)
                INSERT INTO grupos (numero) VALUES (1), (2), (3), (4), (5)
            """);

            System.out.println("✅ Datos iniciales insertados");

        } catch (SQLException e) {
            System.err.println("❌ Error creando tablas: " + e.getMessage());
        }
    }
}