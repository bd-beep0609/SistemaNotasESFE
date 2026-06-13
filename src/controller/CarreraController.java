package controller;

import model.Carrera;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarreraController {

    public List<Carrera> getAllCarreras() {
        List<Carrera> carreras = new ArrayList<>();
        String sql = "SELECT * FROM carreras WHERE activo = 1 ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                carreras.add(new Carrera(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getBoolean("activo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carreras;
    }

    public Carrera findById(int id) {
        String sql = "SELECT * FROM carreras WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Carrera(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getBoolean("activo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getIdByNombre(String nombre) {
        String sql = "SELECT id FROM carreras WHERE nombre = ?";
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