package model;

import java.io.Serializable;

public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private int id;
    private String carnet;
    private String nombre;
    private String apellido;
    private int carreraId;
    private int nivelId;
    private int grupoId;
    private boolean activo;

    // Campos adicionales para mostrar (join)
    private String carreraNombre;
    private String nivelNombre;
    private int grupoNumero;

    // Constructor para nuevo estudiante (sin ID)
    public Estudiante(String carnet, String nombre, String apellido,
                      int carreraId, int nivelId, int grupoId, boolean activo) {
        this.id = nextId++;
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carreraId = carreraId;
        this.nivelId = nivelId;
        this.grupoId = grupoId;
        this.activo = activo;
    }

    // Constructor para editar estudiante (con ID)
    public Estudiante(int id, String carnet, String nombre, String apellido,
                      int carreraId, int nivelId, int grupoId, boolean activo) {
        this.id = id;
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carreraId = carreraId;
        this.nivelId = nivelId;
        this.grupoId = grupoId;
        this.activo = activo;
        if (id >= nextId) nextId = id + 1;
    }

    // Constructor para mostrar desde BD (con nombres)
    public Estudiante(int id, String carnet, String nombre, String apellido,
                      String carreraNombre, String nivelNombre, int grupoNumero, boolean activo) {
        this.id = id;
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carreraNombre = carreraNombre;
        this.nivelNombre = nivelNombre;
        this.grupoNumero = grupoNumero;
        this.activo = activo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public int getCarreraId() { return carreraId; }
    public void setCarreraId(int carreraId) { this.carreraId = carreraId; }

    public int getNivelId() { return nivelId; }
    public void setNivelId(int nivelId) { this.nivelId = nivelId; }

    public int getGrupoId() { return grupoId; }
    public void setGrupoId(int grupoId) { this.grupoId = grupoId; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getActivoText() { return activo ? "Activo" : "Inactivo"; }

    // Getters para mostrar
    public String getCarreraNombre() { return carreraNombre; }
    public String getNivelNombre() { return nivelNombre; }
    public int getGrupoNumero() { return grupoNumero; }

    public String getCursoDisplay() {
        if (carreraNombre != null && nivelNombre != null) {
            if (nivelNombre.equals("4° Articulado")) {
                return carreraNombre + " - " + nivelNombre + " Gpo " + grupoNumero;
            } else {
                return carreraNombre + " - " + nivelNombre + " Año Gpo " + grupoNumero;
            }
        }
        return "Cargando...";
    }

    public static void setNextId(int value) { nextId = value; }
    public static int getNextId() { return nextId; }

    @Override
    public String toString() {
        return carnet + " - " + getNombreCompleto() + " (" + getCursoDisplay() + ")";
    }
}