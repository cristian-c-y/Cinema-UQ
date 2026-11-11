package co.edu.uniquindio.poo.modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorPromociones {

    private final List<Promocion> promociones = new ArrayList<>();

    public void agregarPromocion(Promocion promocion) {

        if (promocion == null) {
            throw new IllegalArgumentException(
                    "La promoción no puede ser nula."
            );
        }

        if (buscarPromocion(promocion.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una promoción con ese ID."
            );
        }

        promociones.add(promocion);
    }

    public List<Promocion> consultarPromociones() {
        return List.copyOf(promociones);
    }

    public Promocion buscarPromocion(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        for (Promocion promocion : promociones) {

            if (promocion.getId().equals(id)) {
                return promocion;
            }
        }

        return null;
    }

    public boolean actualizarPromocion(
            String id,
            String nombre,
            double porcentaje) {

        Promocion promocion = buscarPromocion(id);

        if (promocion == null) {
            return false;
        }

        if (nombre == null || nombre.isBlank()
                || !Double.isFinite(porcentaje)
                || porcentaje < 0
                || porcentaje > 100) {

            throw new IllegalArgumentException(
                    "Los datos de la promoción no son válidos."
            );
        }

        promocion.setNombre(nombre);
        promocion.setPorcentajeDescuento(porcentaje);

        return true;
    }

    public boolean activarPromocion(String id) {

        Promocion promocion = buscarPromocion(id);

        if (promocion == null) {
            return false;
        }

        promocion.activar();
        return true;
    }

    public boolean desactivarPromocion(String id) {

        Promocion promocion = buscarPromocion(id);

        if (promocion == null) {
            return false;
        }

        promocion.desactivar();
        return true;
    }

    public boolean eliminarPromocion(String id) {

        Promocion promocion = buscarPromocion(id);

        if (promocion == null) {
            return false;
        }

        return promociones.remove(promocion);
    }
}

