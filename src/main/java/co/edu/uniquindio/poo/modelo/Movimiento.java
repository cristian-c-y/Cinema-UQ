package co.edu.uniquindio.poo.modelo;

import java.time.LocalDateTime;

public class Movimiento {

    private String tipo;
    private double valor;
    private String descripcion;
    private LocalDateTime fecha;

    public Movimiento(String tipo, double valor, String descripcion) {
        this.tipo = tipo;
        this.valor = valor;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}