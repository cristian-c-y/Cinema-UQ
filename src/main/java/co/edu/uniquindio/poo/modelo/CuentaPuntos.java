package co.edu.uniquindio.poo.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CuentaPuntos {

    private List<LotePuntos> lotes;

    public CuentaPuntos() {
        this.lotes = new ArrayList<>();
    }

    public List<LotePuntos> getLotes() {
        return List.copyOf(lotes);
    }

    public int consultarSaldo() {
        LocalDate hoy = LocalDate.now();

        return lotes.stream()
                .filter(lote -> !lote.getFechaVencimiento().isBefore(hoy))
                .mapToInt(LotePuntos::getCantidadDisponible)
                .sum();
    }

    public void acumularPuntos(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "Los puntos deben ser mayores que cero."
            );
        }

        lotes.add(new LotePuntos(cantidad, LocalDate.now()));
    }

    public boolean redimirPuntos(int cantidad) {
        if (cantidad <= 0 || cantidad > consultarSaldo()) {
            return false;
        }

        LocalDate hoy = LocalDate.now();

        List<LotePuntos> lotesDisponibles = lotes.stream()
                .filter(lote -> !lote.getFechaVencimiento().isBefore(hoy))
                .filter(lote -> lote.getCantidadDisponible() > 0)
                .sorted(Comparator.comparing(LotePuntos::getFechaVencimiento))
                .toList();

        int pendientes = cantidad;

        for (LotePuntos lote : lotesDisponibles) {
            int usados = Math.min(
                    pendientes,
                    lote.getCantidadDisponible()
            );

            lote.reducirCantidad(usados);
            pendientes -= usados;

            if (pendientes == 0) {
                break;
            }
        }

        return true;
    }
}
