
package co.edu.uniquindio.poo.patrones.comportamiento;

public class DescuentoPorcentaje implements EstrategiaDescuento {

    private final double porcentaje;

    public DescuentoPorcentaje(double porcentaje) {

        if (!Double.isFinite(porcentaje)
                || porcentaje < 0
                || porcentaje > 100) {

            throw new IllegalArgumentException(
                    "El descuento debe estar entre 0 y 100."
            );
        }

        this.porcentaje = porcentaje;
    }

    @Override
    public double calcularTotal(double subtotal) {
        return subtotal * (1 - porcentaje / 100.0);
    }
}

