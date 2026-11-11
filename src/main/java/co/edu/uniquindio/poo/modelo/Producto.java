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

        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser un número válido y no negativo."
            );
        }

        this.precio = precio;
    }

    public void setNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del producto no puede estar vacío."
            );
        }

        this.nombre = nombre;
    }
}
