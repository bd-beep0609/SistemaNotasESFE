package controller;

import model.Usuario;
import db.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private Usuario currentUser;

    public Usuario authenticate(String email, String password) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ? AND activo = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentUser = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getBoolean("activo"),
                        rs.getString("rol")
                );
                currentUser.setPassword(password);
                return currentUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario getCurrentUser() { return currentUser; }
    public void logout() { currentUser = null; }

    public boolean addUser(String nombre, String email, String password, boolean activo, String rol) {
        String sql = "INSERT INTO usuarios (nombre, email, password, activo, rol) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setBoolean(4, activo);
            pstmt.setString(5, rol);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(null, "El email ya existe");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean updateUser(int id, String nombre, String email, boolean activo, String rol) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, activo = ?, rol = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.setBoolean(3, activo);
            pstmt.setString(4, rol);
            pstmt.setInt(5, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ? AND email != 'admin@demo.com'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}