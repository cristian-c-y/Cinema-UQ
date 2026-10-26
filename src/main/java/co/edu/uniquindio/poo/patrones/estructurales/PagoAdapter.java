package co.edu.uniquindio.poo.patrones.estructurales;

public class PagoAdapter implements ProcesadorPago {

    private ServicioPagoExterno servicioExterno;

    public PagoAdapter(ServicioPagoExterno servicioExterno) {
        this.servicioExterno = servicioExterno;
    }

    @Override
    public boolean procesarPago(
            double monto,
            String numeroTarjeta) {

        return servicioExterno.realizarTransaccion(
                monto,
                numeroTarjeta
        );
    }

    @Override
    public boolean devolverPago(
            double monto,
            String numeroTarjeta) {

        return servicioExterno.realizarDevolucion(
                monto,
                numeroTarjeta
        );
    }
}