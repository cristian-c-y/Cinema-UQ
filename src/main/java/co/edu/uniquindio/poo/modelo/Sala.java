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

    public void setNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la sala no puede estar vacío."
            );
        }

        this.nombre = nombre;
    }

    public void generarAsientos(int filas, int asientosPorFila) {

        if (filas < 1 || filas > 26 || asientosPorFila < 1) {
            throw new IllegalArgumentException(
                    "La cantidad de filas o asientos no es válida."
            );
        }

        if (!asientos.isEmpty()) {
            throw new IllegalStateException(
                    "La sala ya tiene asientos registrados."
            );
        }

        for (int i = 0; i < filas; i++) {

            String fila = String.valueOf((char) ('A' + i));

            for (int numero = 1; numero <= asientosPorFila; numero++) {

                Asiento asiento = new Asiento(
                        String.valueOf(numero),
                        fila
                );

                agregarAsiento(asiento);
            }
        }
    }
}