package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.modelo.*;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServicioEstadisticas {

    public List<Compra> obtenerTodasLasCompras(
            GestorUsuarios gestorUsuarios) {

        List<Compra> compras = new ArrayList<>();

        for (Usuario usuario : gestorUsuarios.getUsuarios()) {

            if (usuario instanceof Cliente cliente) {
                compras.addAll(cliente.getCompras());
            }
        }

        return compras;
    }

    public Map<String, Integer> entradasVendidasPorPelicula(
            List<Compra> compras) {

        Map<String, Integer> resultado =
                new LinkedHashMap<>();

        for (Compra compra : compras) {

            if (!"PAGADA".equals(compra.getEstado())) {
                continue;
            }

            for (ElementoVenta elemento : compra.getElementos()) {

                if (elemento instanceof Entrada entrada) {

                    String pelicula = entrada.getFuncion()
                            .getPelicula()
                            .getTitulo();

                    resultado.merge(
                            pelicula, 1, Integer::sum
                    );
                }
            }
        }

        return resultado;
    }

    public Map<String, Double> ingresosPorPelicula(
            List<Compra> compras) {

        Map<String, Double> resultado =
                new LinkedHashMap<>();

        for (Compra compra : compras) {

            if (!"PAGADA".equals(compra.getEstado())) {
                continue;
            }

            for (ElementoVenta elemento : compra.getElementos()) {

                if (elemento instanceof Entrada entrada) {

                    String pelicula = entrada.getFuncion()
                            .getPelicula()
                            .getTitulo();

                    resultado.merge(
                            pelicula,
                            entrada.calcularPrecio(),
                            Double::sum
                    );
                }
            }
        }

        return resultado;
    }

    public Map<String, Integer> productosMasVendidos(
            List<Compra> compras) {

        Map<String, Integer> resultado =
                new LinkedHashMap<>();

        for (Compra compra : compras) {

            if (!"PAGADA".equals(compra.getEstado())) {
                continue;
            }

            for (ElementoVenta elemento : compra.getElementos()) {

                if (elemento instanceof Producto
                        || elemento instanceof Combo) {

                    resultado.merge(
                            elemento.getNombre(),
                            1,
                            Integer::sum
                    );
                }
            }
        }

        return resultado;
    }

    public double calcularIngresosTotales(
            List<Compra> compras) {

        double total = 0;

        for (Compra compra : compras) {

            if ("PAGADA".equals(compra.getEstado())) {
                total += compra.getTotal();
            }
        }

        return total;
    }
    public Map<String, Integer> contarComprasPorEstado(
            List<Compra> compras) {

        Map<String, Integer> resultado =
                new LinkedHashMap<>();

        for (Compra compra : compras) {

            resultado.merge(
                    compra.getEstado(),
                    1,
                    Integer::sum
            );
        }

        return resultado;
    }

    public int contarComprasPagadas(
            List<Compra> compras) {

        int cantidad = 0;

        for (Compra compra : compras) {

            if ("PAGADA".equals(compra.getEstado())) {
                cantidad++;
            }
        }

        return cantidad;
    }
}
