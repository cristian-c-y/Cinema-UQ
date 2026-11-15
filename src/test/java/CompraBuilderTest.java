

import co.edu.uniquindio.poo.modelo.Compra;
import co.edu.uniquindio.poo.patrones.creacionales.CompraBuilder;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CompraBuilderTest {

    private ElementoVenta crearProducto(
            String nombre, double precio) {

        return new ElementoVenta() {
            @Override
            public String getNombre() {
                return nombre;
            }

            @Override
            public double calcularPrecio() {
                return precio;
            }
        };
    }

    @Test
    void debeConstruirCompraConElementos() {

        Compra compra = new CompraBuilder()
                .id("COMP-TEST-001")
                .fecha(LocalDateTime.now())
                .agregarElemento(crearProducto("Crispetas", 12000))
                .agregarElemento(crearProducto("Gaseosa", 7000))
                .build();

        assertEquals("COMP-TEST-001", compra.getId());
        assertEquals(2, compra.getElementos().size());
        assertEquals(19000, compra.getSubtotal(), 0.001);
        assertEquals(19000, compra.getTotal(), 0.001);
        assertEquals("CREADA", compra.getEstado());
    }

    @Test
    void debeConservarDetallesDeLaCompra() {

        Compra compra = new CompraBuilder()
                .id("COMP-TEST-002")
                .fecha(LocalDateTime.now())
                .agregarElemento(crearProducto("Nachos", 10000))
                .build();

        assertEquals(1, compra.getDetalles().size());
        assertEquals(
                "Nachos",
                compra.getDetalles().get(0).getNombre()
        );
        assertEquals(
                10000,
                compra.getDetalles().get(0).getPrecioUnitario(),
                0.001
        );
    }
}