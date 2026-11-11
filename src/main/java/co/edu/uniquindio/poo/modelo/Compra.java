package co.edu.uniquindio.poo.modelo;

import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;
import co.edu.uniquindio.poo.patrones.comportamiento.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Compra {

    private String id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private List<ElementoVenta> elementos;

    private double subtotal;
    private double total;

    private final List<DetalleCompra> detalles;

    private EstadoCompra estado;

    private String nombrePromocion;
    private EstrategiaDescuento estrategiaDescuento;

    public Compra(
            String id,
            Cliente cliente,
            LocalDateTime fecha,
            List<ElementoVenta> elementos) {

        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.elementos = List.copyOf(elementos);

        this.detalles = this.elementos.stream()
                .map(elemento -> new DetalleCompra(
                        elemento.getNombre(),
                        elemento.calcularPrecio()
                ))
                .toList();

        this.subtotal = calcularSubtotal();
        this.total = subtotal;

        this.estado = new EstadoCreada();

        this.nombrePromocion = "Sin promoción";
        this.estrategiaDescuento = new SinDescuento();
    }

    private double calcularSubtotal() {

        return detalles.stream()
                .mapToDouble(DetalleCompra::getPrecioUnitario)
                .sum();
    }

    // ==============================
    // PATRÓN STRATEGY
    // ==============================

    public void aplicarPromocion(Promocion promocion) {

        if (!"CREADA".equals(getEstado())) {
            throw new IllegalStateException(
                    "No se pueden modificar promociones de una compra "
                            + "que ya fue pagada o reembolsada."
            );
        }

        if (promocion == null || !promocion.isActiva()) {

            estrategiaDescuento = new SinDescuento();
            nombrePromocion = "Sin promoción";

        } else {

            estrategiaDescuento = new DescuentoPorcentaje(
                    promocion.getPorcentajeDescuento()
            );

            nombrePromocion = promocion.getNombre();
        }

        total = estrategiaDescuento.calcularTotal(subtotal);
    }

    // ==============================
    // PATRÓN STATE
    // ==============================

    public void pagar() {
        estado.pagar(this);
    }

    public void reembolsar() {
        estado.reembolsar(this);
    }

    public void cambiarEstado(EstadoCompra nuevoEstado) {
        this.estado = Objects.requireNonNull(
                nuevoEstado,
                "El estado de la compra no puede ser nulo."
        );
    }

    public String getEstado() {
        return estado.getNombre();
    }


    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public List<ElementoVenta> getElementos() {
        return elementos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotal() {
        return total;
    }

    public String getNombrePromocion() {
        return nombrePromocion;
    }

    public double getValorDescuento() {
        return subtotal - total;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }
}
