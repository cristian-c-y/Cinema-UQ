package co.edu.uniquindio.poo.modelo;

public class Promocion {

    private String id;
    private String nombre;
    private double porcentajeDescuento;

    public Promocion(String id, String nombre,
                     double porcentajeDescuento) {
        this.id = id;
        this.nombre = nombre;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}