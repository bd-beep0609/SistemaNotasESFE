package controller;

import model.Grupo;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoController {

    public List<Grupo> getAllGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT * FROM grupos WHERE activo = 1 ORDER BY numero";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                grupos.add(new Grupo(
                        rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getBoolean("activo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grupos;
    }

    public Grupo findById(int id) {
        String sql = "SELECT * FROM grupos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Grupo(
                        rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getBoolean("activo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getIdByNumero(int numero) {
        String sql = "SELECT id FROM grupos WHERE numero = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numero);
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