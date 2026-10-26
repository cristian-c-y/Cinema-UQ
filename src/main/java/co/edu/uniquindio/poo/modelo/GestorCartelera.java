package co.edu.uniquindio.poo.modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorCartelera {

    private final List<Pelicula> peliculas;
    private final List<Funcion> funciones;

    public GestorCartelera() {
        peliculas = new ArrayList<>();
        funciones = new ArrayList<>();
    }

    public void agregarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser nula.");
        }

        for (Pelicula p : peliculas) {
            if (p.getId().equals(pelicula.getId())) {
                throw new IllegalArgumentException(
                        "Ya existe una película con ese ID.");
            }
        }

        peliculas.add(pelicula);
    }

    public void agregarFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("La función no puede ser nula.");
        }

        boolean peliculaRegistrada = peliculas.stream()
                .anyMatch(p -> p.getId().equals(funcion.getPelicula().getId()));

        if (!peliculaRegistrada) {
            throw new IllegalArgumentException(
                    "La película de la función no está registrada.");
        }

        for (Funcion f : funciones) {
            if (f.getId().equals(funcion.getId())) {
                throw new IllegalArgumentException(
                        "Ya existe una función con ese ID.");
            }
        }

        funciones.add(funcion);
    }

    public List<Pelicula> consultarPeliculas() {
        return List.copyOf(peliculas);
    }

    public List<Funcion> consultarFunciones() {
        return List.copyOf(funciones);
    }

    public List<Funcion> consultarFuncionesPorPelicula(String idPelicula) {
        List<Funcion> resultado = new ArrayList<>();

        for (Funcion funcion : funciones) {
            if (funcion.getPelicula().getId().equals(idPelicula)) {
                resultado.add(funcion);
            }
        }

        return resultado;
    }
}
