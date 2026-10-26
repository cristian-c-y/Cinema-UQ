package co.edu.uniquindio.poo.modelo;

import co.edu.uniquindio.poo.patrones.estructurales.ElementoPrecio;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

public class Entrada implements ElementoVenta, ElementoPrecio {

    private String id;
    private Funcion funcion;
    private Asiento asiento;
    private double precio;

    public Entrada(String id, Funcion funcion, Asiento asiento, double precio) {
        this.id = id;
        this.funcion = funcion;
        this.asiento = asiento;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    @Override
    public String getNombre() {
        return "Entrada - " + funcion.getPelicula().getTitulo();
    }

    @Override
    public double calcularPrecio() {
        return precio;
    }
}
