package co.edu.uniquindio.poo.patrones.estructurales;

import java.util.Objects;

public abstract class DecoradorPrecio implements ElementoVenta {

    protected final ElementoVenta elemento;

    public DecoradorPrecio(ElementoVenta elemento) {
        this.elemento = Objects.requireNonNull(
                elemento,
                "El producto no puede ser nulo"
        );
    }

    @Override
    public String getNombre() {
        return elemento.getNombre();
    }

    @Override
    public double calcularPrecio() {
        return elemento.calcularPrecio();
    }
}
