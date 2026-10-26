package co.edu.uniquindio.poo.patrones.creacionales;

public class ConfiguracionCinemaUQ {

    private static ConfiguracionCinemaUQ instancia;

    private String nombreCine;
    private double iva;
    private int porcentajePuntos;

    private ConfiguracionCinemaUQ() {
        nombreCine = "CinemaUQ";
        iva = 0.19;
        porcentajePuntos = 10;
    }

    public static ConfiguracionCinemaUQ getInstancia() {

        if (instancia == null) {
            instancia = new ConfiguracionCinemaUQ();
        }

        return instancia;
    }

    public String getNombreCine() {
        return nombreCine;
    }

    public double getIva() {
        return iva;
    }

    public int getPorcentajePuntos() {
        return porcentajePuntos;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public void setPorcentajePuntos(int porcentajePuntos) {
        this.porcentajePuntos = porcentajePuntos;
    }
}