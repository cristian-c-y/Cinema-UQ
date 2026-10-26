package co.edu.uniquindio.poo.patrones.creacionales;

import co.edu.uniquindio.poo.modelo.Producto;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

public class CreadorProducto extends CreadorElemento {

    private String id;
    private String nombre;
    private double precio;

    public CreadorProducto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public ElementoVenta crearElemento() {
        return new Producto(id, nombre, precio);
    }
}
