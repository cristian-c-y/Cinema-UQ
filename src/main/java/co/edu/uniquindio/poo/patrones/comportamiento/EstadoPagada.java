package co.edu.uniquindio.poo.patrones.comportamiento;

import co.edu.uniquindio.poo.modelo.Compra;

public class EstadoPagada implements EstadoCompra {

    @Override
    public void pagar(Compra compra) {
        throw new IllegalStateException(
                "La compra ya está pagada."
        );
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.cambiarEstado(new EstadoReembolsada());
    }

    @Override
    public String getNombre() {
        return "PAGADA";
    }
}
