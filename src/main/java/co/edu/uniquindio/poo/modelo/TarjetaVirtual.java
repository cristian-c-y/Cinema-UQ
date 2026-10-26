package co.edu.uniquindio.poo.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TarjetaVirtual {

    private String numero;
    private double saldo;
    private boolean activa;
    private List<Movimiento> movimientos;

    public TarjetaVirtual() {
        this.numero = UUID.randomUUID().toString()
                .substring(0, 8).toUpperCase();
        this.saldo = 0;
        this.activa = true;
        this.movimientos = new ArrayList<>();
    }

    public String getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isActiva() {
        return activa;
    }

    public List<Movimiento> getMovimientos() {
        return List.copyOf(movimientos);
    }

    public void recargar(double valor, Administrador administrador) {
        if (administrador == null) {
            throw new IllegalArgumentException(
                    "Solo un administrador puede autorizar la recarga."
            );
        }

        if (valor <= 0) {
            throw new IllegalArgumentException(
                    "El valor de la recarga debe ser positivo."
            );
        }

        if (!activa) {
            throw new IllegalStateException("La tarjeta está inactiva.");
        }

        saldo += valor;
        registrarMovimiento("RECARGA", valor, "Recarga autorizada");
    }

    public boolean debitar(double valor) {
        if (!activa || valor <= 0 || valor > saldo) {
            return false;
        }

        saldo -= valor;
        registrarMovimiento("COMPRA", valor, "Pago de compra");
        return true;
    }

    public void reembolsar(double valor, String descripcion) {
        if (valor <= 0) {
            throw new IllegalArgumentException(
                    "El valor del reembolso debe ser positivo."
            );
        }

        saldo += valor;
        registrarMovimiento("REEMBOLSO", valor, descripcion);
    }

    public void desactivar() {
        activa = false;
    }

    public void activar() {
        activa = true;
    }

    private void registrarMovimiento(
            String tipo, double valor, String descripcion) {
        movimientos.add(new Movimiento(tipo, valor, descripcion));
    }
}
