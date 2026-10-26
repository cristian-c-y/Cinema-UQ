package co.edu.uniquindio.poo.patrones.estructurales;

public class ServicioPagoExterno {

    public boolean realizarTransaccion(double valor, String tarjeta) {
        if (valor <= 0 || tarjeta == null || tarjeta.isBlank()) {
            return false;
        }

        System.out.println(
                "Autorización externa simulada para un pago de $" + valor
        );

        return true;
    }

    public boolean realizarDevolucion(double valor, String tarjeta) {
        if (valor <= 0 || tarjeta == null || tarjeta.isBlank()) {
            return false;
        }

        System.out.println(
                "Autorización externa simulada para una devolución de $" + valor
        );

        return true;
    }
}
