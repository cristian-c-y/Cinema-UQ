package co.edu.uniquindio.poo.patrones.comportamiento;

import co.edu.uniquindio.poo.modelo.Compra;

public class ObservadorNotificacionCompra
        implements ObservadorCompra {

    @Override
    public void actualizar(Compra compra) {

        System.out.println(
                "[CinemaUQ] Compra confirmada: "
                        + compra.getId()
                        + " | Cliente: "
                        + compra.getCliente().getNombre()
                        + " | Total: $"
                        + String.format("%,.0f", compra.getTotal())
        );
    }
}
