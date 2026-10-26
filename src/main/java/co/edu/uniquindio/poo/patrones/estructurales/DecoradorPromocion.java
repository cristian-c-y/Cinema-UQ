package co.edu.uniquindio.poo.patrones.estructurales;

import co.edu.uniquindio.poo.modelo.Promocion;

public class DecoradorPromocion extends DecoradorPrecio {

    private Promocion promocion;

    public DecoradorPromocion(
            ElementoPrecio elemento,
            Promocion promocion) {

        super(elemento);
        this.promocion = promocion;
    }

    @Override
    public double calcularPrecio() {

        double precioOriginal =
                elemento.calcularPrecio();

        double descuento =
                precioOriginal *
                        promocion.getPorcentajeDescuento() / 100;

        return precioOriginal - descuento;
    }
}
