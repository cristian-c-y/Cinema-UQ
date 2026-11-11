package co.edu.uniquindio.poo.patrones.estructurales;

import co.edu.uniquindio.poo.modelo.*;

import co.edu.uniquindio.poo.patrones.comportamiento.NotificadorCompra;
import co.edu.uniquindio.poo.patrones.comportamiento.ObservadorNotificacionCompra;
import co.edu.uniquindio.poo.patrones.comportamiento.ObservadorNotificacionPuntos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompraFacade {

    private final GestorAsientos gestorAsientos;
    private final GestorPuntos gestorPuntos;
    private final ProcesadorPago procesadorPago;

    // Patrón Observer
    private final NotificadorCompra notificadorCompras;

    public CompraFacade(
            GestorAsientos gestorAsientos,
            GestorPuntos gestorPuntos,
            ProcesadorPago procesadorPago) {

        this.gestorAsientos = gestorAsientos;
        this.gestorPuntos = gestorPuntos;
        this.procesadorPago = procesadorPago;

        this.notificadorCompras = new NotificadorCompra();

        notificadorCompras.agregarObservador(
                new ObservadorNotificacionCompra()
        );

        notificadorCompras.agregarObservador(
                new ObservadorNotificacionPuntos()
        );
    }

    public NotificadorCompra getNotificadorCompras() {
        return notificadorCompras;
    }

    private void notificarCompraExitosa(Compra compra) {
        try {
            notificadorCompras.notificarCompra(compra);
        } catch (RuntimeException e) {
            System.err.println(
                    "Error al notificar la compra: " + e.getMessage()
            );
        }
    }

    private void finalizarCompra(Cliente cliente, Compra compra) {

        compra.pagar();

        cliente.agregarCompra(compra);

        gestorPuntos.acumularPuntos(
                cliente,
                compra.getTotal()
        );

        notificarCompraExitosa(compra);
    }

    private boolean compraDisponibleParaPago(Compra compra) {
        return "CREADA".equals(compra.getEstado());
    }


    public boolean realizarCompra(
            Cliente cliente,
            Compra compra,
            Funcion funcion,
            Asiento asiento,
            String numeroTarjeta) {

        if (cliente == null || compra == null
                || funcion == null || asiento == null) {

            throw new IllegalArgumentException(
                    "Los datos de la compra son obligatorios."
            );
        }

        if (compra.getCliente() != cliente) {
            throw new IllegalArgumentException(
                    "La compra no pertenece al cliente indicado."
            );
        }

        if (numeroTarjeta == null || numeroTarjeta.isBlank()) {
            throw new IllegalArgumentException(
                    "El número de tarjeta es obligatorio."
            );
        }

        if (!compraDisponibleParaPago(compra)) {
            System.out.println(
                    "La compra no está en estado CREADA."
            );
            return false;
        }

        var tarjeta = cliente.getTarjetaVirtual();
        double total = compra.getTotal();

        if (tarjeta == null || !tarjeta.isActiva()) {
            System.out.println("La tarjeta virtual está inactiva.");
            return false;
        }

        if (!Double.isFinite(total)
                || total <= 0
                || tarjeta.getSaldo() < total) {

            System.out.println("Saldo insuficiente o total inválido.");
            return false;
        }

        if (!gestorAsientos.reservarAsiento(funcion, asiento)) {
            System.out.println("El asiento ya está reservado.");
            return false;
        }

        boolean autorizado;

        try {
            autorizado = procesadorPago.procesarPago(
                    total, numeroTarjeta
            );
        } catch (RuntimeException e) {
            gestorAsientos.liberarAsiento(funcion, asiento);
            throw e;
        }

        if (!autorizado) {
            gestorAsientos.liberarAsiento(funcion, asiento);
            System.out.println("El pago fue rechazado.");
            return false;
        }

        if (!tarjeta.debitar(total)) {

            try {
                boolean devolucionExitosa =
                        procesadorPago.devolverPago(
                                total, numeroTarjeta
                        );

                if (!devolucionExitosa) {
                    System.err.println(
                            "ATENCIÓN: devolución externa pendiente."
                    );
                }
            } finally {
                gestorAsientos.liberarAsiento(funcion, asiento);
            }

            return false;
        }

        finalizarCompra(cliente, compra);

        System.out.println("Compra realizada correctamente.");
        System.out.println("Total: $" + total);
        System.out.println(
                "Saldo restante: $" + tarjeta.getSaldo()
        );

        return true;
    }


    public boolean realizarCompraMultiple(
            Cliente cliente,
            Compra compra,
            Funcion funcion,
            List<Asiento> asientos,
            String numeroTarjeta) {

        if (cliente == null || compra == null
                || funcion == null
                || asientos == null || asientos.isEmpty()) {

            throw new IllegalArgumentException(
                    "Los datos de la compra son obligatorios."
            );
        }

        if (compra.getCliente() != cliente) {
            throw new IllegalArgumentException(
                    "La compra no pertenece al cliente indicado."
            );
        }

        if (numeroTarjeta == null || numeroTarjeta.isBlank()) {
            throw new IllegalArgumentException(
                    "El número de tarjeta es obligatorio."
            );
        }

        if (!compraDisponibleParaPago(compra)) {
            System.out.println(
                    "No se puede volver a pagar esta compra."
            );
            return false;
        }

        var tarjeta = cliente.getTarjetaVirtual();
        double total = compra.getTotal();

        if (tarjeta == null || !tarjeta.isActiva()
                || !Double.isFinite(total)
                || total <= 0
                || tarjeta.getSaldo() < total) {

            return false;
        }

        Set<String> claves = new HashSet<>();

        for (Asiento asiento : asientos) {

            if (asiento == null) {
                return false;
            }

            String clave =
                    asiento.getFila() + "-" + asiento.getNumero();

            if (!claves.add(clave)) {
                return false;
            }

            boolean perteneceSala = funcion.getSala()
                    .getAsientos()
                    .stream()
                    .anyMatch(a ->
                            a.getFila().equals(asiento.getFila())
                                    && a.getNumero().equals(
                                    asiento.getNumero()
                            )
                    );

            if (!perteneceSala
                    || !gestorAsientos.asientoDisponible(
                    funcion, asiento
            )) {

                return false;
            }
        }

        List<Asiento> reservados = new ArrayList<>();

        for (Asiento asiento : asientos) {

            if (!gestorAsientos.reservarAsiento(
                    funcion, asiento
            )) {
                liberarReservados(funcion, reservados);
                return false;
            }

            reservados.add(asiento);
        }

        boolean autorizado;

        try {
            autorizado = procesadorPago.procesarPago(
                    total, numeroTarjeta
            );
        } catch (RuntimeException e) {
            liberarReservados(funcion, reservados);
            throw e;
        }

        if (!autorizado) {
            liberarReservados(funcion, reservados);
            return false;
        }

        if (!tarjeta.debitar(total)) {

            try {
                boolean devolucionExitosa =
                        procesadorPago.devolverPago(
                                total, numeroTarjeta
                        );

                if (!devolucionExitosa) {
                    System.err.println(
                            "ATENCIÓN: devolución externa pendiente."
                    );
                }
            } finally {
                liberarReservados(funcion, reservados);
            }

            return false;
        }

        finalizarCompra(cliente, compra);

        return true;
    }

    private void liberarReservados(
            Funcion funcion,
            List<Asiento> asientos) {

        for (Asiento asiento : asientos) {
            gestorAsientos.liberarAsiento(funcion, asiento);
        }
    }


    public boolean realizarCompraConfiteria(
            Cliente cliente,
            Compra compra,
            String numeroTarjeta) {

        if (cliente == null || compra == null) {
            throw new IllegalArgumentException(
                    "Los datos de la compra son obligatorios."
            );
        }

        if (compra.getCliente() != cliente) {
            throw new IllegalArgumentException(
                    "La compra no pertenece al cliente indicado."
            );
        }

        if (numeroTarjeta == null || numeroTarjeta.isBlank()) {
            throw new IllegalArgumentException(
                    "El número de tarjeta es obligatorio."
            );
        }

        if (compra.getElementos().isEmpty()) {
            throw new IllegalArgumentException(
                    "La compra debe contener productos."
            );
        }

        if (!compraDisponibleParaPago(compra)) {
            System.out.println(
                    "Esta compra ya fue procesada."
            );
            return false;
        }

        var tarjeta = cliente.getTarjetaVirtual();
        double total = compra.getTotal();

        if (tarjeta == null || !tarjeta.isActiva()) {
            return false;
        }

        if (!Double.isFinite(total)
                || total <= 0
                || tarjeta.getSaldo() < total) {

            return false;
        }

        boolean autorizado = procesadorPago.procesarPago(
                total, numeroTarjeta
        );

        if (!autorizado) {
            return false;
        }

        if (!tarjeta.debitar(total)) {

            boolean devolucionExitosa =
                    procesadorPago.devolverPago(
                            total, numeroTarjeta
                    );

            if (!devolucionExitosa) {
                System.err.println(
                        "ATENCIÓN: devolución externa pendiente."
                );
            }

            return false;
        }

        finalizarCompra(cliente, compra);

        return true;
    }
}
