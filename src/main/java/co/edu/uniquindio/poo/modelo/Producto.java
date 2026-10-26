package co.edu.uniquindio.poo.modelo;

import co.edu.uniquindio.poo.patrones.estructurales.ElementoConfiteria;

public class Producto implements ElementoConfiteria {

    private String id;
    private String nombre;
    private double precio;

    public Producto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double calcularPrecio() {
        return precio;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
