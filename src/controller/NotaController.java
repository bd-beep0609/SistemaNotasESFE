package controller;

import model.Nota;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaController {

    public List<Nota> getAllNotas() {
        List<Nota> notas = new ArrayList<>();
        String sql = """
            SELECT n.id, n.estudiante_id, n.modulo_id, n.valor, n.fecha, n.observacion,
                   CONCAT(e.nombre, ' ', e.apellido) as estudiante_nombre,
                   m.nombre as modulo_nombre
            FROM notas n
            JOIN estudiantes e ON n.estudiante_id = e.id
            JOIN modulos m ON n.modulo_id = m.id
            ORDER BY n.fecha DESC
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Nota nota = new Nota(
                        rs.getInt("id"),
                        rs.getInt("estudiante_id"),
                        rs.getInt("modulo_id"),
                        rs.getDouble("valor"),
                        rs.getDate("fecha"),
                        rs.getString("observacion")
                );
                nota.setEstudianteNombre(rs.getString("estudiante_nombre"));
                nota.setModuloNombre(rs.getString("modulo_nombre"));
                notas.add(nota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notas;
    }

    public double getPromedioByEstudiante(int estudianteId) {
        String sql = "SELECT AVG(CAST(valor AS DECIMAL(3,1))) as promedio FROM notas WHERE estudiante_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estudianteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double promedio = rs.getDouble("promedio");
                return Math.round(promedio * 100.0) / 100.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addNota(int estudianteId, int moduloId, double valor, String observacion) {
        if (valor < 0 || valor > 10) return false;

        String sql = "INSERT INTO notas (estudiante_id, modulo_id, valor, observacion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estudianteId);
            pstmt.setInt(2, moduloId);
            pstmt.setDouble(3, valor);
            pstmt.setString(4, observacion != null ? observacion : "");
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNota(int id) {
        String sql = "DELETE FROM notas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}