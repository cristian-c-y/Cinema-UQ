import co.edu.uniquindio.poo.patrones.creacionales.GeneradorConsecutivos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GeneradorConsecutivosTest {

    @Test
    void debeRetornarLaMismaInstancia() {

        GeneradorConsecutivos instancia1 =
                GeneradorConsecutivos.getInstancia();

        GeneradorConsecutivos instancia2 =
                GeneradorConsecutivos.getInstancia();

        assertSame(instancia1, instancia2);
    }

    @Test
    void debeGenerarIdentificadoresConsecutivos() {

        GeneradorConsecutivos generador =
                GeneradorConsecutivos.getInstancia();

        String primero = generador.generarIdCompra();
        String segundo = generador.generarIdCompra();

        int numeroPrimero = Integer.parseInt(
                primero.substring(5)
        );

        int numeroSegundo = Integer.parseInt(
                segundo.substring(5)
        );

        assertEquals(numeroPrimero + 1, numeroSegundo);
        assertTrue(primero.startsWith("COMP-"));
        assertTrue(segundo.startsWith("COMP-"));
    }
}