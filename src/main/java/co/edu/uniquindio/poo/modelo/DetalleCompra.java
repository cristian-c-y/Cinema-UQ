package co.edu.uniquindio.poo.modelo;

public class DetalleCompra {

    private final String nombre;
    private final double precioUnitario;

    public DetalleCompra(String nombre, double precioUnitario) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del producto es obligatorio."
            );
        }

        if (!Double.isFinite(precioUnitario) || precioUnitario < 0) {
            throw new IllegalArgumentException(
                    "El precio del producto no es válido."
            );
        }

        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
}

