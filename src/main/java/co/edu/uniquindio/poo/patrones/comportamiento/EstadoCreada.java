package co.edu.uniquindio.poo.patrones.comportamiento;

import co.edu.uniquindio.poo.modelo.Compra;

public class EstadoCreada implements EstadoCompra {

    @Override
    public void pagar(Compra compra) {
        compra.cambiarEstado(new EstadoPagada());
    }

    @Override
    public void reembolsar(Compra compra) {
        throw new IllegalStateException(
                "No se puede reembolsar una compra que no ha sido pagada."
        );
    }

    @Override
    public String getNombre() {
        return "CREADA";
    }
}

