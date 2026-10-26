package co.edu.uniquindio.poo.modelo;

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

        catalogo.add(elemento);
    }

    public List<ElementoVenta> consultarCatalogo() {
        return List.copyOf(catalogo);
    }
}
