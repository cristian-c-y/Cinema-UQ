package co.edu.uniquindio.poo.modelo;

public class Promocion {

    private String id;
    private String nombre;
    private double porcentajeDescuento;
    private boolean activa;

    public Promocion(String id, String nombre,
                     double porcentajeDescuento) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El ID de la promoción es obligatorio."
            );
        }

        this.id = id;
        setNombre(nombre);
        setPorcentajeDescuento(porcentajeDescuento);
        this.activa = true;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la promoción es obligatorio."
            );
        }

        this.nombre = nombre;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {

        if (!Double.isFinite(porcentajeDescuento)
                || porcentajeDescuento < 0
                || porcentajeDescuento > 100) {

            throw new IllegalArgumentException(
                    "El descuento debe estar entre 0 y 100."
            );
        }

        this.porcentajeDescuento = porcentajeDescuento;
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }

    public double aplicarDescuento(double precio) {

        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException(
                    "El precio no es válido."
            );
        }

        if (!activa) {
            return precio;
        }

        return precio * (1 - porcentajeDescuento / 100.0);
    }
}
