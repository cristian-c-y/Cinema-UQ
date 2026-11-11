
package co.edu.uniquindio.poo.patrones.comportamiento;

import co.edu.uniquindio.poo.modelo.Compra;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificadorCompra {

    private final List<ObservadorCompra> observadores =
            new ArrayList<>();

    public void agregarObservador(ObservadorCompra observador) {
        Objects.requireNonNull(observador);

        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void eliminarObservador(ObservadorCompra observador) {
        observadores.remove(observador);
    }

    public void notificarCompra(Compra compra) {
        Objects.requireNonNull(compra);

        for (ObservadorCompra observador :
                new ArrayList<>(observadores)) {

            observador.actualizar(compra);
        }
    }
}

