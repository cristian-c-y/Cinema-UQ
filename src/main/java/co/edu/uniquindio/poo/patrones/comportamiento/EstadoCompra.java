package co.edu.uniquindio.poo.patrones.comportamiento;

import co.edu.uniquindio.poo.modelo.Compra;

public interface EstadoCompra {

    void pagar(Compra compra);

    void reembolsar(Compra compra);

    String getNombre();
}

