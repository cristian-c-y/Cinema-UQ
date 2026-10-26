package co.edu.uniquindio.poo.modelo;

public class Pelicula {

    private String id;
    private String titulo;
    private String genero;
    private int duracionMinutos;
    private String clasificacion;

    public Pelicula(String id, String titulo, String genero,
                    int duracionMinutos, String clasificacion) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}
