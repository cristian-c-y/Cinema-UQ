package co.edu.uniquindio.poo.patrones.comportamiento;

import co.edu.uniquindio.poo.modelo.Compra;

public class ObservadorNotificacionPuntos
        implements ObservadorCompra {

    @Override
    public void actualizar(Compra compra) {

        int puntosGanados = (int) (compra.getTotal() / 1000);

        System.out.println(
                "[CinemaUQ] El cliente "
                        + compra.getCliente().getNombre()
                        + " ganó "
                        + puntosGanados
                        + " puntos por la compra "
                        + compra.getId()
        );
    }
}
