package co.edu.uniquindio.poo.servicios;

import java.io.IOException;

public interface ExportadorReporte {

    void exportar(String contenido, String ruta)
            throws IOException;
}
