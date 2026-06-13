package model;

public class Grupo {
    private int id;
    private int numero;
    private boolean activo;

    public Grupo(int id, int numero, boolean activo) {
        this.id = id;
        this.numero = numero;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.valueOf(numero);
    }
}