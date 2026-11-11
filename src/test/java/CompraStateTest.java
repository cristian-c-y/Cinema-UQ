import co.edu.uniquindio.poo.modelo.Compra;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompraStateTest {

    private Compra compra;

    @BeforeEach
    void prepararCompra() {
        compra = new Compra(
                "COMP-TEST-001",
                null,
                LocalDateTime.now(),
                List.of()
        );
    }

    @Test
    void compraDebeIniciarCreada() {
        assertEquals("CREADA", compra.getEstado());
    }

    @Test
    void pagarCompraDebeCambiarEstado() {
        compra.pagar();

        assertEquals("PAGADA", compra.getEstado());
    }

    @Test
    void noDebePermitirPagarDosVeces() {
        compra.pagar();

        assertThrows(
                IllegalStateException.class,
                () -> compra.pagar()
        );

        assertEquals("PAGADA", compra.getEstado());
    }

    @Test
    void noDebePermitirReembolsarCompraCreada() {
        assertThrows(
                IllegalStateException.class,
                () -> compra.reembolsar()
        );

        assertEquals("CREADA", compra.getEstado());
    }

    @Test
    void reembolsarCompraPagadaDebeCambiarEstado() {
        compra.pagar();
        compra.reembolsar();

        assertEquals("REEMBOLSADA", compra.getEstado());
    }

    @Test
    void noDebePermitirReembolsarDosVeces() {
        compra.pagar();
        compra.reembolsar();

        assertThrows(
                IllegalStateException.class,
                () -> compra.reembolsar()
        );

        assertEquals("REEMBOLSADA", compra.getEstado());
    }
}

