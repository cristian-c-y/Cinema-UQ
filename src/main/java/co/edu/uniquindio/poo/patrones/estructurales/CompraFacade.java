package co.edu.uniquindio.poo.patrones.estructurales;

import co.edu.uniquindio.poo.modelo.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompraFacade {

    private GestorAsientos gestorAsientos;
    private GestorPuntos gestorPuntos;
    private ProcesadorPago procesadorPago;

    public CompraFacade(
            GestorAsientos gestorAsientos,
            GestorPuntos gestorPuntos,
            ProcesadorPago procesadorPago) {

        this.gestorAsientos = gestorAsientos;
        this.gestorPuntos = gestorPuntos;
        this.procesadorPago = procesadorPago;
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

        var tarjeta = cliente.getTarjetaVirtual();
        double total = compra.getTotal();

        if (tarjeta == null || !tarjeta.isActiva()) {
            System.out.println("La tarjeta virtual está inactiva.");
            return false;
        }

        if (total <= 0 || tarjeta.getSaldo() < total) {
            System.out.println("Saldo insuficiente.");
            return false;
        }

        // Reservar el asiento antes de autorizar el pago.
        if (!gestorAsientos.reservarAsiento(funcion, asiento)) {
            System.out.println("El asiento ya está reservado.");
            return false;
        }

        // Solicitar autorización al servicio externo simulado.
        boolean autorizado = procesadorPago.procesarPago(
                total, numeroTarjeta
        );

        if (!autorizado) {
            gestorAsientos.liberarAsiento(funcion, asiento);
            System.out.println("El pago fue rechazado.");
            return false;
        }

        // Descontar el saldo interno una sola vez.
        if (!tarjeta.debitar(total)) {
            boolean devolucionExitosa =
                    procesadorPago.devolverPago(total, numeroTarjeta);

            gestorAsientos.liberarAsiento(funcion, asiento);

            if (!devolucionExitosa) {
                System.out.println(
                        "ATENCIÓN: la devolución externa requiere revisión."
                );
            }

            System.out.println("No fue posible completar el débito.");
            return false;
        }

        cliente.agregarCompra(compra);
        gestorPuntos.acumularPuntos(cliente, total);

        System.out.println("Compra realizada correctamente.");
        System.out.println("Total: $" + total);
        System.out.println("Saldo restante: $" + tarjeta.getSaldo());

        return true;
    }

    public boolean realizarCompraMultiple(
            Cliente cliente,
            Compra compra,
            Funcion funcion,
            List<Asiento> asientos,
            String numeroTarjeta) {

        if (cliente == null || compra == null || funcion == null
                || asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Los datos de la compra son obligatorios.");
        }

        if (compra.getCliente() != cliente) {
            throw new IllegalArgumentException(
                    "La compra no pertenece al cliente indicado.");
        }

        if (numeroTarjeta == null || numeroTarjeta.isBlank()) {
            throw new IllegalArgumentException(
                    "El número de tarjeta es obligatorio.");
        }

        var tarjeta = cliente.getTarjetaVirtual();
        double total = compra.getTotal();

        if (tarjeta == null || !tarjeta.isActiva()
                || total <= 0 || tarjeta.getSaldo() < total) {
            return false;
        }

        // Verificar que los asientos pertenezcan a la sala
        // y que no existan asientos repetidos.
        Set<String> claves = new HashSet<>();

        for (Asiento asiento : asientos) {

            if (asiento == null) {
                return false;
            }

            String clave = asiento.getFila() + "-" + asiento.getNumero();

            if (!claves.add(clave)) {
                return false;
            }

            boolean perteneceSala = funcion.getSala()
                    .getAsientos()
                    .stream()
                    .anyMatch(a ->
                            a.getFila().equals(asiento.getFila())
                                    && a.getNumero().equals(asiento.getNumero()));

            if (!perteneceSala ||
                    !gestorAsientos.asientoDisponible(funcion, asiento)) {
                return false;
            }
        }

        // Reservar temporalmente todos los asientos
        List<Asiento> reservados = new ArrayList<>();

        for (Asiento asiento : asientos) {

            if (!gestorAsientos.reservarAsiento(funcion, asiento)) {

                for (Asiento reservado : reservados) {
                    gestorAsientos.liberarAsiento(funcion, reservado);
                }

                return false;
            }

            reservados.add(asiento);
        }

        boolean autorizado;

        try {
            autorizado = procesadorPago.procesarPago(total, numeroTarjeta);
        } catch (RuntimeException e) {
            liberarReservados(funcion, reservados);
            throw e;
        }

        if (!autorizado) {
            liberarReservados(funcion, reservados);
            return false;
        }

        if (!tarjeta.debitar(total)) {

            boolean devolucionExitosa =
                    procesadorPago.devolverPago(total, numeroTarjeta);

            liberarReservados(funcion, reservados);

            if (!devolucionExitosa) {
                System.err.println(
                        "ATENCIÓN: devolución pendiente de revisión.");
            }

            return false;
        }

        cliente.agregarCompra(compra);
        gestorPuntos.acumularPuntos(cliente, total);

        return true;
    }

    private void liberarReservados(
            Funcion funcion, List<Asiento> asientos) {

        for (Asiento asiento : asientos) {
            gestorAsientos.liberarAsiento(funcion, asiento);
        }
    }
}