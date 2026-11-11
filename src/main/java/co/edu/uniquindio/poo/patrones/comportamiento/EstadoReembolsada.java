
package co.edu.uniquindio.poo.patrones.comportamiento;

import co.edu.uniquindio.poo.modelo.Compra;

public class EstadoReembolsada implements EstadoCompra {

    @Override
    public void pagar(Compra compra) {
        throw new IllegalStateException(
                "Una compra reembolsada no puede volver a pagarse."
        );
    }

    @Override
    public void reembolsar(Compra compra) {
        throw new IllegalStateException(
                "La compra ya fue reembolsada."
        );
    }

    @Override
    public String getNombre() {
        return "REEMBOLSADA";
    }
}

