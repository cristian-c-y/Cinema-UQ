package co.edu.uniquindio.poo.patrones.estructurales;

public interface ProcesadorPago {

    boolean procesarPago(double monto, String numeroTarjeta);
    boolean devolverPago(double monto, String numeroTarjeta);
}
