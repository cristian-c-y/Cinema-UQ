package co.edu.uniquindio.poo.modelo;

import java.time.LocalDateTime;

public class Funcion {

    private String id;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDateTime fechaHora;

    public Funcion(String id, Pelicula pelicula,
                   Sala sala, LocalDateTime fechaHora) {
        this.id = id;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fechaHora = fechaHora;
    }

    public String getId() {
        return id;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}