package co.edu.uniquindio.poo.patrones.estructurales;

public class DecoradorMantequilla extends DecoradorPrecio {

    private static final double PRECIO_EXTRA = 2000;

    public DecoradorMantequilla(ElementoVenta elemento) {
        super(elemento);
    }

    @Override
    public String getNombre() {
        return elemento.getNombre() + " + Mantequilla";
    }

    @Override
    public double calcularPrecio() {
        return elemento.calcularPrecio() + PRECIO_EXTRA;
    }
}

