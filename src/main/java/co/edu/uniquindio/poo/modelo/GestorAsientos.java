package co.edu.uniquindio.poo.modelo;

import java.util.HashSet;
import java.util.Set;

public class GestorAsientos {

    private Set<String> asientosReservados;

    public GestorAsientos() {
        this.asientosReservados = new HashSet<>();
    }

    public boolean asientoDisponible(Funcion funcion, Asiento asiento) {
        if (funcion == null || asiento == null) {
            return false;
        }

        String clave = generarClave(funcion, asiento);

        return !asientosReservados.contains(clave);
    }

    public boolean reservarAsiento(Funcion funcion, Asiento asiento) {
        if (funcion == null || asiento == null) {
            return false;
        }

        String clave = generarClave(funcion, asiento);

        // add devuelve false si la clave ya existe.
        return asientosReservados.add(clave);
    }

    public void liberarAsiento(Funcion funcion, Asiento asiento) {
        if (funcion == null || asiento == null) {
            throw new IllegalArgumentException(
                    "La función y el asiento son obligatorios."
            );
        }

        asientosReservados.remove(generarClave(funcion, asiento));
    }

    private String generarClave(Funcion funcion, Asiento asiento) {
        return funcion.getId() + "-"
                + asiento.getFila() + "-"
                + asiento.getNumero();
    }
}
