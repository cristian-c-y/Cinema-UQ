package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.modelo.Compra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class GeneradorReporteVentas {

    private final ServicioEstadisticas estadisticas;

    public GeneradorReporteVentas(
            ServicioEstadisticas estadisticas) {

        this.estadisticas = estadisticas;
    }

    public String generar(List<Compra> compras) {

        StringBuilder reporte = new StringBuilder();

        reporte.append("====================================\n");
        reporte.append("          CINEMAUQ\n");
        reporte.append("       REPORTE DE VENTAS\n");
        reporte.append("====================================\n\n");

        reporte.append("Fecha de generación: ")
                .append(LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy HH:mm"
                        )
                ))
                .append("\n\n");

        reporte.append("RESUMEN GENERAL\n");
        reporte.append("------------------------------------\n");

        reporte.append("Compras pagadas: ")
                .append(estadisticas.contarComprasPagadas(compras))
                .append("\n");

        reporte.append(String.format(
                "Ingresos totales: $%,.2f COP%n%n",
                estadisticas.calcularIngresosTotales(compras)
        ));

        reporte.append("ENTRADAS VENDIDAS POR PELÍCULA\n");
        reporte.append("------------------------------------\n");

        Map<String, Integer> entradas =
                estadisticas.entradasVendidasPorPelicula(compras);

        entradas.forEach((pelicula, cantidad) ->
                reporte.append(pelicula)
                        .append(": ")
                        .append(cantidad)
                        .append("\n")
        );

        reporte.append("\nINGRESOS BRUTOS POR PELÍCULA\n");
        reporte.append("------------------------------------\n");

        Map<String, Double> ingresos =
                estadisticas.ingresosPorPelicula(compras);

        ingresos.forEach((pelicula, total) ->
                reporte.append(String.format(
                        "%s: $%,.2f COP%n",
                        pelicula, total
                ))
        );

        reporte.append("\nPRODUCTOS Y COMBOS VENDIDOS\n");
        reporte.append("------------------------------------\n");

        Map<String, Integer> productos =
                estadisticas.productosMasVendidos(compras);

        productos.forEach((producto, cantidad) ->
                reporte.append(producto)
                        .append(": ")
                        .append(cantidad)
                        .append("\n")
        );

        reporte.append("\n====================================\n");
        reporte.append("       FIN DEL REPORTE\n");
        reporte.append("====================================\n");

        return reporte.toString();
    }
}
