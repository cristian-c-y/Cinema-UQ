
package co.edu.uniquindio.poo.patrones.creacionales;

public final class GeneradorConsecutivos {

    private static final GeneradorConsecutivos INSTANCIA =
            new GeneradorConsecutivos();

    private int consecutivoCompra;

    // Constructor privado
    private GeneradorConsecutivos() {
        this.consecutivoCompra = 0;
    }

    // Único punto de acceso a la instancia
    public static GeneradorConsecutivos getInstancia() {
        return INSTANCIA;
    }

    public synchronized String generarIdCompra() {
        consecutivoCompra++;

        return String.format(
                "COMP-%04d",
                consecutivoCompra
        );
    }
}

