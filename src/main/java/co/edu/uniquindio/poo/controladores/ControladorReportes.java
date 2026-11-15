package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.modelo.Compra;
import co.edu.uniquindio.poo.modelo.GestorUsuarios;
import co.edu.uniquindio.poo.servicios.ExportadorReporte;
import co.edu.uniquindio.poo.servicios.ExportadorTXT;
import co.edu.uniquindio.poo.servicios.GeneradorReporteVentas;
import co.edu.uniquindio.poo.servicios.ServicioEstadisticas;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ControladorReportes {

    @FXML private Label lblComprasPagadas;
    @FXML private Label lblIngresosTotales;

    @FXML
    private BarChart<String, Number> graficoVentas;

    private GestorUsuarios gestorUsuarios;

    private final ServicioEstadisticas estadisticas =
            new ServicioEstadisticas();

    private final ExportadorReporte exportador =
            new ExportadorTXT();

    private final GeneradorReporteVentas generador =
            new GeneradorReporteVentas(estadisticas);

    public void configurarGestorUsuarios(
            GestorUsuarios gestorUsuarios) {

        this.gestorUsuarios = gestorUsuarios;
        actualizarEstadisticas();
    }

    @FXML
    private void actualizarEstadisticas() {

        if (gestorUsuarios == null) {
            return;
        }

        List<Compra> compras =
                estadisticas.obtenerTodasLasCompras(
                        gestorUsuarios
                );

        int comprasPagadas =
                estadisticas.contarComprasPagadas(compras);

        double ingresos =
                estadisticas.calcularIngresosTotales(compras);

        lblComprasPagadas.setText(
                String.valueOf(comprasPagadas)
        );

        lblIngresosTotales.setText(
                String.format(
                        Locale.forLanguageTag("es-CO"),
                        "%,.0f COP",
                        ingresos
                )
        );

        Map<String, Integer> ventas =
                estadisticas.entradasVendidasPorPelicula(
                        compras
                );

        mostrarGrafico(ventas);
    }

    private void mostrarGrafico(
            Map<String, Integer> ventas) {

        graficoVentas.getData().clear();

        XYChart.Series<String, Number> serie =
                new XYChart.Series<>();

        serie.setName("Entradas vendidas");

        for (Map.Entry<String, Integer> dato
                : ventas.entrySet()) {

            serie.getData().add(
                    new XYChart.Data<>(
                            dato.getKey(),
                            dato.getValue()
                    )
            );
        }

        graficoVentas.getData().add(serie);
    }

    @FXML
    private void exportarReporte() {

        if (gestorUsuarios == null) {
            return;
        }

        FileChooser selector = new FileChooser();

        selector.setTitle("Guardar reporte de ventas");
        selector.setInitialFileName("reporte_ventas_cinemauq.txt");

        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Archivos de texto (*.txt)",
                        "*.txt"
                )
        );

        File archivo = selector.showSaveDialog(
                graficoVentas.getScene().getWindow()
        );

        if (archivo == null) {
            return;
        }

        try {

            List<Compra> compras =
                    estadisticas.obtenerTodasLasCompras(
                            gestorUsuarios
                    );

            String contenido =
                    generador.generar(compras);

            exportador.exportar(
                    contenido,
                    archivo.getAbsolutePath()
            );

            mostrarMensaje(
                    Alert.AlertType.INFORMATION,
                    "Reporte exportado",
                    "El reporte se guardó correctamente."
            );

        } catch (IOException | IllegalArgumentException e) {

            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Error al exportar",
                    e.getMessage()
            );
        }
    }

    private void mostrarMensaje(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
