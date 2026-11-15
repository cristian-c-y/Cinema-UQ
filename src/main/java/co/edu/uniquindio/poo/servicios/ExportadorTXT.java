package co.edu.uniquindio.poo.servicios;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExportadorTXT implements ExportadorReporte {

    @Override
    public void exportar(String contenido, String ruta)
            throws IOException {

        if (contenido == null || contenido.isBlank()) {
            throw new IllegalArgumentException(
                    "El reporte no puede estar vacío."
            );
        }

        if (ruta == null || ruta.isBlank()) {
            throw new IllegalArgumentException(
                    "La ruta del archivo no puede estar vacía."
            );
        }

        Path archivo = Path.of(ruta);

        if (!archivo.toString().toLowerCase().endsWith(".txt")) {
            archivo = Path.of(ruta + ".txt");
        }

        Path directorio = archivo.getParent();

        if (directorio != null) {
            Files.createDirectories(directorio);
        }

        Files.writeString(
                archivo,
                contenido,
                StandardCharsets.UTF_8
        );
    }
}

