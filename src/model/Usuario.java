package model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private int id;
    private String nombre;
    private String email;
    private String password;
    private boolean activo;
    private String rol;  // "ADMIN" o "USUARIO"

    // Constructor para nuevo usuario (sin ID)
    public Usuario(String nombre, String email, String password, boolean activo, String rol) {
        this.id = nextId++;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.activo = activo;
        this.rol = rol;
    }

    // Constructor para editar usuario (con ID)
    public Usuario(int id, String nombre, String email, String password, boolean activo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.activo = activo;
        this.rol = rol;
        if (id >= nextId) nextId = id + 1;
    }

    // Constructor para mostrar desde BD (sin password)
    public Usuario(int id, String nombre, String email, boolean activo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = "";
        this.activo = activo;
        this.rol = rol;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getActivoText() { return activo ? "Activo" : "Inactivo"; }

    public boolean isAdmin() { return "ADMIN".equals(rol); }
    public boolean isUsuario() { return "USUARIO".equals(rol); }

    public static void setNextId(int value) { nextId = value; }
    public static int getNextId() { return nextId; }

    @Override
    public String toString() {
        return nombre + " (" + email + ") - " + rol;
    }
}