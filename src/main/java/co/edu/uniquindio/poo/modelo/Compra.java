package co.edu.uniquindio.poo.modelo;

import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private String id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private List<ElementoVenta> elementos;
    private double total;

    public Compra(String id, Cliente cliente, LocalDateTime fecha,
                  List<ElementoVenta> elementos) {

        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.elementos = elementos;
        this.total = calcularTotal();
    }

    private double calcularTotal() {
        return elementos.stream()
                .mapToDouble(ElementoVenta::calcularPrecio)
                .sum();
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

    public double getTotal() {
        return total;
    }
}