package model;

public class Modulo {
    private int id;
    private String nombre;
    private int carreraId;
    private int nivelId;
    private int grupoId;

    // Campos adicionales para mostrar
    private String carreraNombre;
    private String nivelNombre;
    private int grupoNumero;

    // Constructor para crear (sin ID)
    public Modulo(String nombre, int carreraId, int nivelId, int grupoId) {
        this.nombre = nombre;
        this.carreraId = carreraId;
        this.nivelId = nivelId;
        this.grupoId = grupoId;
    }

    // Constructor para mostrar desde BD (con join)
    public Modulo(int id, String nombre, String carreraNombre, String nivelNombre, int grupoNumero) {
        this.id = id;
        this.nombre = nombre;
        this.carreraNombre = carreraNombre;
        this.nivelNombre = nivelNombre;
        this.grupoNumero = grupoNumero;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCarreraId() { return carreraId; }
    public int getNivelId() { return nivelId; }
    public int getGrupoId() { return grupoId; }
    public String getCarreraNombre() { return carreraNombre; }
    public String getNivelNombre() { return nivelNombre; }
    public int getGrupoNumero() { return grupoNumero; }

    public String getDisplayName() {
        if (carreraNombre != null) {
            if (nivelNombre.equals("4° Articulado")) {
                return carreraNombre + " - " + nivelNombre + " Gpo " + grupoNumero + " - " + nombre;
            } else {
                return carreraNombre + " - " + nivelNombre + " Año Gpo " + grupoNumero + " - " + nombre;
            }
        }
        return nombre;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}