package co.edu.uniquindio.poo.patrones.estructurales;

public abstract class DecoradorPrecio implements ElementoPrecio {

    protected ElementoPrecio elemento;

    public DecoradorPrecio(ElementoPrecio elemento) {
        this.elemento = elemento;
    }

    @Override
    public double calcularPrecio() {
        return elemento.calcularPrecio();
    }
}
