package controller;

import model.Modulo;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuloController {

    public List<Modulo> getAllModulos() {
        List<Modulo> modulos = new ArrayList<>();
        String sql = """
            SELECT m.id, m.nombre, 
                   c.nombre as carrera_nombre, 
                   n.nombre as nivel_nombre, 
                   g.numero as grupo_numero
            FROM modulos m
            JOIN carreras c ON m.carrera_id = c.id
            JOIN niveles n ON m.nivel_id = n.id
            JOIN grupos g ON m.grupo_id = g.id
            ORDER BY c.nombre, n.orden, g.numero
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                modulos.add(new Modulo(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("carrera_nombre"),
                        rs.getString("nivel_nombre"),
                        rs.getInt("grupo_numero")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modulos;
    }

    public List<Modulo> getModulosByCarrera(int carreraId) {
        List<Modulo> modulos = new ArrayList<>();
        String sql = """
            SELECT m.id, m.nombre, 
                   c.nombre as carrera_nombre, 
                   n.nombre as nivel_nombre, 
                   g.numero as grupo_numero
            FROM modulos m
            JOIN carreras c ON m.carrera_id = c.id
            JOIN niveles n ON m.nivel_id = n.id
            JOIN grupos g ON m.grupo_id = g.id
            WHERE m.carrera_id = ?
            ORDER BY n.orden, g.numero
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carreraId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                modulos.add(new Modulo(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("carrera_nombre"),
                        rs.getString("nivel_nombre"),
                        rs.getInt("grupo_numero")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modulos;
    }

    public Modulo findById(int id) {
        String sql = """
            SELECT m.id, m.nombre, 
                   c.nombre as carrera_nombre, 
                   n.nombre as nivel_nombre, 
                   g.numero as grupo_numero
            FROM modulos m
            JOIN carreras c ON m.carrera_id = c.id
            JOIN niveles n ON m.nivel_id = n.id
            JOIN grupos g ON m.grupo_id = g.id
            WHERE m.id = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Modulo(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("carrera_nombre"),
                        rs.getString("nivel_nombre"),
                        rs.getInt("grupo_numero")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}