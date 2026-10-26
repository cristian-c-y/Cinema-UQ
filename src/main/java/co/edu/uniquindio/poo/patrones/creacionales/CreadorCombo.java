package co.edu.uniquindio.poo.patrones.creacionales;

import co.edu.uniquindio.poo.modelo.Combo;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

public class CreadorCombo extends CreadorElemento {

    private String id;
    private String nombre;

    public CreadorCombo(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public ElementoVenta crearElemento() {
        return new Combo(id, nombre);
    }
}
