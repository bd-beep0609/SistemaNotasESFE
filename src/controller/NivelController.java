package controller;

import model.Nivel;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NivelController {

    public List<Nivel> getAllNiveles() {
        List<Nivel> niveles = new ArrayList<>();
        String sql = "SELECT * FROM niveles WHERE activo = 1 ORDER BY orden";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                niveles.add(new Nivel(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("orden"),
                        rs.getBoolean("activo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return niveles;
    }

    public Nivel findById(int id) {
        String sql = "SELECT * FROM niveles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Nivel(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("orden"),
                        rs.getBoolean("activo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getIdByNombre(String nombre) {
        String sql = "SELECT id FROM niveles WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}