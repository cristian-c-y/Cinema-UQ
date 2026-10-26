package co.edu.uniquindio.poo.modelo;

import java.time.LocalDate;

public class LotePuntos {

    private int cantidadDisponible;
    private LocalDate fechaObtencion;
    private LocalDate fechaVencimiento;

    public LotePuntos(int cantidad, LocalDate fechaObtencion) {
        if (cantidad <= 0 || fechaObtencion == null) {
            throw new IllegalArgumentException(
                    "La cantidad y la fecha deben ser válidas."
            );
        }

        this.cantidadDisponible = cantidad;
        this.fechaObtencion = fechaObtencion;
        this.fechaVencimiento = fechaObtencion.plusYears(1);
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public LocalDate getFechaObtencion() {
        return fechaObtencion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void reducirCantidad(int cantidad) {
        if (cantidad <= 0 || cantidad > cantidadDisponible) {
            throw new IllegalArgumentException(
                    "La cantidad a reducir no es válida."
            );
        }

        cantidadDisponible -= cantidad;
    }
}
