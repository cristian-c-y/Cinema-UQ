package co.edu.uniquindio.poo.modelo;

import java.util.ArrayList;
import java.util.List;

public class Sala {

    private String id;
    private String nombre;
    private List<Asiento> asientos;

    public Sala(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.asientos = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void agregarAsiento(Asiento asiento) {
        asientos.add(asiento);
    }
}