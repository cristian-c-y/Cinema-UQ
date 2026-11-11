package co.edu.uniquindio.poo.patrones.estructurales;

public class DecoradorQueso extends DecoradorPrecio {

    private static final double PRECIO_EXTRA = 3000;

    public DecoradorQueso(ElementoVenta elemento) {
        super(elemento);
    }

    @Override
    public String getNombre() {
        return elemento.getNombre() + " + Queso";
    }

    @Override
    public double calcularPrecio() {
        return elemento.calcularPrecio() + PRECIO_EXTRA;
    }
}

