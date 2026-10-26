package co.edu.uniquindio.poo.patrones.creacionales;

import co.edu.uniquindio.poo.modelo.Asiento;
import co.edu.uniquindio.poo.modelo.Entrada;
import co.edu.uniquindio.poo.modelo.Funcion;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

public class CreadorEntrada extends CreadorElemento {

    private String id;
    private Funcion funcion;
    private Asiento asiento;
    private double precio;

    public CreadorEntrada(String id, Funcion funcion,
                          Asiento asiento, double precio) {
        this.id = id;
        this.funcion = funcion;
        this.asiento = asiento;
        this.precio = precio;
    }

    @Override
    public ElementoVenta crearElemento() {
        return new Entrada(id, funcion, asiento, precio);
    }
}
