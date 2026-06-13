package model;

public class Nivel {
    private int id;
    private String nombre;
    private int orden;
    private boolean activo;

    public Nivel(int id, String nombre, int orden, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.orden = orden;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return nombre;
    }
}