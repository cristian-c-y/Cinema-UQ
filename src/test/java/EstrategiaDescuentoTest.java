import co.edu.uniquindio.poo.patrones.comportamiento.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstrategiaDescuentoTest {

    @Test
    void sinDescuentoDebeConservarPrecioOriginal() {

        EstrategiaDescuento estrategia = new SinDescuento();

        double total = estrategia.calcularTotal(20000);

        assertEquals(20000, total, 0.001);
    }

    @Test
    void descuentoVeintePorciento() {

        EstrategiaDescuento estrategia =
                new DescuentoPorcentaje(20);

        double total = estrategia.calcularTotal(20000);

        assertEquals(16000, total, 0.001);
    }

    @Test
    void descuentoCincuentaPorciento() {

        EstrategiaDescuento estrategia =
                new DescuentoPorcentaje(50);

        double total = estrategia.calcularTotal(20000);

        assertEquals(10000, total, 0.001);
    }

    @Test
    void descuentoCienPorciento() {

        EstrategiaDescuento estrategia =
                new DescuentoPorcentaje(100);

        double total = estrategia.calcularTotal(20000);

        assertEquals(0, total, 0.001);
    }
}

