package co.edu.uniquindio.poo.modelo;

import co.edu.uniquindio.poo.patrones.estructurales.ElementoConfiteria;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

import java.util.ArrayList;
import java.util.List;

public class GestorConfiteria {

    private final List<ElementoVenta> catalogo = new ArrayList<>();

    public void agregarElemento(ElementoVenta elemento) {

        if (elemento == null) {
            throw new IllegalArgumentException(
                    "El producto o combo no puede ser nulo."
            );
        }

        String id = obtenerId(elemento);

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El ID del elemento es obligatorio."
            );
        }

        if (buscarElemento(id) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un producto o combo con ese ID."
            );
        }

        catalogo.add(elemento);
    }

    private String obtenerId(ElementoVenta elemento) {

        if (elemento instanceof Producto producto) {
            return producto.getId();
        }

        if (elemento instanceof Combo combo) {
            return combo.getId();
        }

        return null;
    }

    public ElementoVenta buscarElemento(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        for (ElementoVenta elemento : catalogo) {

            if (id.equals(obtenerId(elemento))) {
                return elemento;
            }
        }

        return null;
    }

    public boolean actualizarProducto(
            String id,
            String nuevoNombre,
            double nuevoPrecio) {

        ElementoVenta elemento = buscarElemento(id);

        if (!(elemento instanceof Producto producto)) {
            return false;
        }

        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del producto es obligatorio."
            );
        }

        if (!Double.isFinite(nuevoPrecio) || nuevoPrecio < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo ni inválido."
            );
        }

        producto.setNombre(nuevoNombre);
        producto.setPrecio(nuevoPrecio);

        return true;
    }

    public boolean actualizarCombo(String id, String nuevoNombre) {

        ElementoVenta elemento = buscarElemento(id);

        if (!(elemento instanceof Combo combo)) {
            return false;
        }

        combo.setNombre(nuevoNombre);

        return true;
    }

    private boolean estaIncluidoEnCombo(
            ElementoConfiteria elementoBuscado) {

        for (ElementoVenta elemento : catalogo) {

            if (elemento instanceof Combo combo) {

                if (contieneElemento(combo, elementoBuscado)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean contieneElemento(
            Combo combo,
            ElementoConfiteria buscado) {

        for (ElementoConfiteria elemento : combo.getElementos()) {

            if (elemento == buscado) {
                return true;
            }

            if (elemento instanceof Combo comboInterno
                    && contieneElemento(comboInterno, buscado)) {
                return true;
            }
        }

        return false;
    }

    public boolean eliminarElemento(String id) {

        ElementoVenta elemento = buscarElemento(id);

        if (elemento == null) {
            return false;
        }

        if (elemento instanceof ElementoConfiteria elementoConfiteria) {

            if (estaIncluidoEnCombo(elementoConfiteria)) {
                throw new IllegalStateException(
                        "No se puede eliminar porque forma parte de un combo."
                );
            }
        }

        return catalogo.remove(elemento);
    }

    public List<ElementoVenta> consultarCatalogo() {
        return List.copyOf(catalogo);
    }
}
