package controller;

import model.Estudiante;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteController {

    // Obtener todos los estudiantes
    public List<Estudiante> getAllEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = """
            SELECT e.id, e.carnet, e.nombre, e.apellido, e.activo,
                   c.nombre as carrera_nombre, 
                   n.nombre as nivel_nombre, 
                   g.numero as grupo_numero
            FROM estudiantes e
            JOIN carreras c ON e.carrera_id = c.id
            JOIN niveles n ON e.nivel_id = n.id
            JOIN grupos g ON e.grupo_id = g.id
            ORDER BY e.id
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                estudiantes.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("carnet"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("carrera_nombre"),
                        rs.getString("nivel_nombre"),
                        rs.getInt("grupo_numero"),
                        rs.getBoolean("activo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estudiantes;
    }

    // Obtener estudiantes activos
    public List<Estudiante> getEstudiantesActivos() {
        List<Estudiante> activos = new ArrayList<>();
        String sql = """
            SELECT e.id, e.carnet, e.nombre, e.apellido, e.activo,
                   c.nombre as carrera_nombre, 
                   n.nombre as nivel_nombre, 
                   g.numero as grupo_numero
            FROM estudiantes e
            JOIN carreras c ON e.carrera_id = c.id
            JOIN niveles n ON e.nivel_id = n.id
            JOIN grupos g ON e.grupo_id = g.id
            WHERE e.activo = 1
            ORDER BY e.nombre
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                activos.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("carnet"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("carrera_nombre"),
                        rs.getString("nivel_nombre"),
                        rs.getInt("grupo_numero"),
                        rs.getBoolean("activo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activos;
    }

    // Buscar por ID
    public Estudiante findById(int id) {
        String sql = """
            SELECT e.id, e.carnet, e.nombre, e.apellido, e.activo,
                   c.nombre as carrera_nombre, 
                   n.nombre as nivel_nombre, 
                   g.numero as grupo_numero
            FROM estudiantes e
            JOIN carreras c ON e.carrera_id = c.id
            JOIN niveles n ON e.nivel_id = n.id
            JOIN grupos g ON e.grupo_id = g.id
            WHERE e.id = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Estudiante(
                        rs.getInt("id"),
                        rs.getString("carnet"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("carrera_nombre"),
                        rs.getString("nivel_nombre"),
                        rs.getInt("grupo_numero"),
                        rs.getBoolean("activo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Agregar estudiante
    public boolean addEstudiante(String carnet, String nombre, String apellido,
                                 int carreraId, int nivelId, int grupoId, boolean activo) {
        String sql = "INSERT INTO estudiantes (carnet, nombre, apellido, carrera_id, nivel_id, grupo_id, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carnet);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setInt(4, carreraId);
            pstmt.setInt(5, nivelId);
            pstmt.setInt(6, grupoId);
            pstmt.setBoolean(7, activo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.err.println("❌ Carnet ya existe");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    // Actualizar estudiante
    public boolean updateEstudiante(int id, String carnet, String nombre, String apellido,
                                    int carreraId, int nivelId, int grupoId, boolean activo) {
        String sql = "UPDATE estudiantes SET carnet = ?, nombre = ?, apellido = ?, carrera_id = ?, nivel_id = ?, grupo_id = ?, activo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carnet);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setInt(4, carreraId);
            pstmt.setInt(5, nivelId);
            pstmt.setInt(6, grupoId);
            pstmt.setBoolean(7, activo);
            pstmt.setInt(8, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar estudiante
    public boolean deleteEstudiante(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cantidad por carrera
    public int getCantidadEstudiantesByCarreraNombre(String carreraNombre) {
        String sql = "SELECT COUNT(*) FROM estudiantes e JOIN carreras c ON e.carrera_id = c.id WHERE c.nombre = ? AND e.activo = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carreraNombre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Estudiantes por carrera (para reportes)
    public List<Estudiante> getEstudiantesByCarreraNombre(String carreraNombre) {
        List<Estudiante> result = new ArrayList<>();
        String sql = """
            SELECT e.id, e.carnet, e.nombre, e.apellido, e.activo,
                   c.nombre as carrera_nombre, 
                   n.nombre as nivel_nombre, 
                   g.numero as grupo_numero
            FROM estudiantes e
            JOIN carreras c ON e.carrera_id = c.id
            JOIN niveles n ON e.nivel_id = n.id
            JOIN grupos g ON e.grupo_id = g.id
            WHERE c.nombre = ? AND e.activo = 1
            ORDER BY e.nombre
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carreraNombre);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("carnet"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("carrera_nombre"),
                        rs.getString("nivel_nombre"),
                        rs.getInt("grupo_numero"),
                        rs.getBoolean("activo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}