package co.edu.uniquindio.poo.modelo;

public class GestorPuntos {

    public void acumularPuntos(Cliente cliente, double valorCompra) {
        if (cliente == null || valorCompra <= 0) {
            throw new IllegalArgumentException(
                    "El cliente y el valor de la compra deben ser válidos."
            );
        }

        int puntos = (int) (valorCompra / 1000);

        if (puntos > 0) {
            cliente.getCuentaPuntos().acumularPuntos(puntos);
        }

        System.out.println(
                "Se acumularon " + puntos
                        + " puntos al cliente "
                        + cliente.getNombre()
        );
    }

    public int consultarPuntos(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El cliente no puede ser nulo."
            );
        }

        return cliente.getCuentaPuntos().consultarSaldo();
    }

    public boolean redimirPuntos(Cliente cliente, int cantidad) {
        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El cliente no puede ser nulo."
            );
        }

        return cliente.getCuentaPuntos().redimirPuntos(cantidad);
    }
}