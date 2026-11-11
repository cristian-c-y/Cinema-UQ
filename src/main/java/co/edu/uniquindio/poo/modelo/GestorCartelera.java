package co.edu.uniquindio.poo.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestorCartelera {

    private final List<Pelicula> peliculas;
    private final List<Funcion> funciones;
    private final List<Sala> salas;

    public GestorCartelera() {
        peliculas = new ArrayList<>();
        funciones = new ArrayList<>();
        salas = new ArrayList<>();
    }

    public void agregarSala(Sala sala) {

        if (sala == null) {
            throw new IllegalArgumentException(
                    "La sala no puede ser nula."
            );
        }

        boolean existe = salas.stream()
                .anyMatch(s -> s.getId().equals(sala.getId()));

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe una sala con ese ID."
            );
        }

        salas.add(sala);
    }

    public List<Sala> consultarSalas() {
        return List.copyOf(salas);
    }

    public Sala buscarSala(String id) {

        for (Sala sala : salas) {
            if (sala.getId().equals(id)) {
                return sala;
            }
        }

        return null;
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

        validarHorario(
                funcion.getPelicula(),
                funcion.getSala(),
                funcion.getFechaHora(),
                null
        );

        funciones.add(funcion);
    }

    private void validarHorario(
            Pelicula pelicula,
            Sala sala,
            LocalDateTime fechaHora,
            String idExcluir) {

        if (pelicula == null || sala == null || fechaHora == null) {
            throw new IllegalArgumentException(
                    "La película, sala y fecha son obligatorias."
            );
        }

        if (!fechaHora.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "La función debe programarse para una fecha futura."
            );
        }

        LocalDateTime finNuevo = fechaHora.plusMinutes(
                pelicula.getDuracionMinutos()
        );

        for (Funcion existente : funciones) {

            if (existente.getId().equals(idExcluir)) {
                continue;
            }

            if (!existente.getSala().getId().equals(sala.getId())) {
                continue;
            }

            LocalDateTime inicioExistente = existente.getFechaHora();

            LocalDateTime finExistente = inicioExistente.plusMinutes(
                    existente.getPelicula().getDuracionMinutos()
            );

            boolean seCruzan =
                    fechaHora.isBefore(finExistente)
                            && inicioExistente.isBefore(finNuevo);

            if (seCruzan) {
                throw new IllegalStateException(
                        "La sala ya tiene una función en ese horario."
                );
            }
        }
    }

    public List<Pelicula> consultarPeliculas() {
        return List.copyOf(peliculas);
    }

    public Pelicula buscarPelicula(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId().equals(id)) {
                return pelicula;
            }
        }

        return null;
    }

    public boolean actualizarFuncion(
            String id,
            LocalDateTime nuevaFechaHora,
            GestorAsientos gestorAsientos) {

        Funcion funcion = buscarFuncion(id);

        if (funcion == null) {
            return false;
        }

        if (nuevaFechaHora == null ||
                !nuevaFechaHora.isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "La fecha y hora deben ser futuras."
            );
        }

        if (gestorAsientos == null) {
            throw new IllegalArgumentException(
                    "El gestor de asientos es obligatorio."
            );
        }

        if (gestorAsientos.tieneAsientosReservados(funcion)) {
            throw new IllegalStateException(
                    "No se puede modificar una función con asientos reservados."
            );
        }

        validarHorario(
                funcion.getPelicula(),
                funcion.getSala(),
                nuevaFechaHora,
                funcion.getId()
        );

        funcion.setFechaHora(nuevaFechaHora);

        return true;
    }

    public boolean eliminarFuncion(
            String id,
            GestorAsientos gestorAsientos) {

        Funcion funcion = buscarFuncion(id);

        if (funcion == null) {
            return false;
        }

        if (gestorAsientos == null) {
            throw new IllegalArgumentException(
                    "El gestor de asientos es obligatorio."
            );
        }

        if (gestorAsientos.tieneAsientosReservados(funcion)) {
            throw new IllegalStateException(
                    "No se puede eliminar una función con asientos reservados."
            );
        }

        return funciones.remove(funcion);
    }

    public boolean eliminarPelicula(String id) {

        Pelicula pelicula = buscarPelicula(id);

        if (pelicula == null) {
            return false;
        }

        boolean tieneFunciones = funciones.stream()
                .anyMatch(funcion ->
                        funcion.getPelicula().getId().equals(id));

        if (tieneFunciones) {
            throw new IllegalStateException(
                    "No puedes eliminar una película que tiene funciones registradas."
            );
        }

        return peliculas.remove(pelicula);
    }

    public boolean actualizarPelicula(
            String id,
            String titulo,
            String genero,
            int duracionMinutos,
            String clasificacion) {

        Pelicula pelicula = buscarPelicula(id);

        if (pelicula == null) {
            return false;
        }

        if (titulo == null || titulo.isBlank()
                || genero == null || genero.isBlank()
                || duracionMinutos <= 0
                || clasificacion == null || clasificacion.isBlank()) {

            throw new IllegalArgumentException(
                    "Los datos de la película no son válidos."
            );
        }

        pelicula.setTitulo(titulo);
        pelicula.setGenero(genero);
        pelicula.setDuracionMinutos(duracionMinutos);
        pelicula.setClasificacion(clasificacion);

        return true;
    }

    public boolean actualizarSala(String id, String nuevoNombre) {
        Sala sala = buscarSala(id);

        if (sala == null) {
            return false;
        }

        sala.setNombre(nuevoNombre);

        return true;
    }

    public boolean eliminarSala(String id) {

        Sala sala = buscarSala(id);

        if (sala == null) {
            return false;
        }

        boolean tieneFunciones = funciones.stream()
                .anyMatch(funcion ->
                        funcion.getSala().getId().equals(id)
                );

        if (tieneFunciones) {
            throw new IllegalStateException(
                    "No se puede eliminar una sala con funciones programadas."
            );
        }

        return salas.remove(sala);
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

    public Funcion buscarFuncion(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        for (Funcion funcion : funciones) {
            if (funcion.getId().equals(id)) {
                return funcion;
            }
        }

        return null;
    }
}
