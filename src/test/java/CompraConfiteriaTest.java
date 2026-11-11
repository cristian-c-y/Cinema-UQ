import co.edu.uniquindio.poo.modelo.*;
import co.edu.uniquindio.poo.patrones.creacionales.CompraBuilder;
import co.edu.uniquindio.poo.patrones.estructurales.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CompraConfiteriaTest {

    private Cliente cliente;
    private CompraFacade compraFacade;

    @BeforeEach
    void preparar() {

        cliente = new Cliente(
                "C001",
                "Cliente de prueba",
                "cliente@cinemauq.com",
                "1234"
        );

        Administrador administrador = new Administrador(
                "A001",
                "Administrador",
                "admin@cinemauq.com",
                "admin123"
        );

        cliente.getTarjetaVirtual().recargar(
                100000,
                administrador
        );

        compraFacade = new CompraFacade(
                new GestorAsientos(),
                new GestorPuntos(),
                new PagoAdapter(new ServicioPagoExterno())
        );
    }

    @Test
    void compraConfiteriaExitosa() {

        Producto crispetas = new Producto(
                "PR001",
                "Crispetas grandes",
                12000
        );

        Producto gaseosa = new Producto(
                "PR002",
                "Gaseosa grande",
                7000
        );

        Compra compra = new CompraBuilder()
                .id("COMP001")
                .cliente(cliente)
                .fecha(LocalDateTime.now())
                .agregarElemento(crispetas)
                .agregarElemento(gaseosa)
                .build();

        boolean resultado =
                compraFacade.realizarCompraConfiteria(
                        cliente,
                        compra,
                        cliente.getTarjetaVirtual().getNumero()
                );

        assertTrue(resultado);

        assertEquals(
                81000,
                cliente.getTarjetaVirtual().getSaldo(),
                0.001
        );

        assertEquals(
                1,
                cliente.getCompras().size()
        );

        assertEquals(
                19,
                cliente.getCuentaPuntos().consultarSaldo()
        );

        assertEquals(
                2,
                cliente.getTarjetaVirtual().getMovimientos().size()
        );
    }

    @Test
    void compraConSaldoInsuficiente() {

        Producto producto = new Producto(
                "PR003",
                "Producto costoso",
                150000
        );

        Compra compra = new CompraBuilder()
                .id("COMP002")
                .cliente(cliente)
                .fecha(LocalDateTime.now())
                .agregarElemento(producto)
                .build();

        boolean resultado =
                compraFacade.realizarCompraConfiteria(
                        cliente,
                        compra,
                        cliente.getTarjetaVirtual().getNumero()
                );

        assertFalse(resultado);

        assertEquals(
                100000,
                cliente.getTarjetaVirtual().getSaldo(),
                0.001
        );

        assertTrue(cliente.getCompras().isEmpty());

        assertEquals(
                0,
                cliente.getCuentaPuntos().consultarSaldo()
        );
    }

    @Test
    void compraConPagoRechazado() {

        Producto producto = new Producto(
                "PR004",
                "Nachos",
                10000
        );

        Compra compra = new CompraBuilder()
                .id("COMP003")
                .cliente(cliente)
                .fecha(LocalDateTime.now())
                .agregarElemento(producto)
                .build();

        boolean resultado =
                compraFacade.realizarCompraConfiteria(
                        cliente,
                        compra,
                        "RECHAZADA"
                );

        assertFalse(resultado);

        assertEquals(
                100000,
                cliente.getTarjetaVirtual().getSaldo(),
                0.001
        );

        assertTrue(cliente.getCompras().isEmpty());
    }
}
