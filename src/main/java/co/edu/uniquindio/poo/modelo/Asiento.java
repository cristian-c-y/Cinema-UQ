package co.edu.uniquindio.poo.modelo;

public class Asiento {

    private String numero;
    private String fila;

    public Asiento(String numero, String fila) {
        this.numero = numero;
        this.fila = fila;
    }

    public String getNumero() {
        return numero;
    }

    public String getFila() {
        return fila;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }
}
