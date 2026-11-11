
package co.edu.uniquindio.poo.patrones.comportamiento;

public class SinDescuento implements EstrategiaDescuento {

    @Override
    public double calcularTotal(double subtotal) {
        return subtotal;
    }
}
