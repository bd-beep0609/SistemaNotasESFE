package model;

import java.io.Serializable;
import java.util.Date;

public class Nota implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int estudianteId;
    private int moduloId;
    private double valor;
    private Date fecha;
    private String observacion;

    // Campos adicionales para mostrar
    private String estudianteNombre;
    private String moduloNombre;

    public Nota(int estudianteId, int moduloId, double valor, String observacion) {
        this.estudianteId = estudianteId;
        this.moduloId = moduloId;
        this.valor = valor;
        this.fecha = new Date();
        this.observacion = observacion;
    }

    public Nota(int id, int estudianteId, int moduloId, double valor, Date fecha, String observacion) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.moduloId = moduloId;
        this.valor = valor;
        this.fecha = fecha;
        this.observacion = observacion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEstudianteId() { return estudianteId; }
    public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }

    public int getModuloId() { return moduloId; }
    public void setModuloId(int moduloId) { this.moduloId = moduloId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public String getModuloNombre() { return moduloNombre; }
    public void setModuloNombre(String moduloNombre) { this.moduloNombre = moduloNombre; }
}