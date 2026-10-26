package co.edu.uniquindio.poo.patrones.creacionales;

import co.edu.uniquindio.poo.modelo.Cliente;
import co.edu.uniquindio.poo.modelo.Compra;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompraBuilder {

    private String id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private List<ElementoVenta> elementos = new ArrayList<>();

    public CompraBuilder id(String id) {
        this.id = id;
        return this;
    }

    public CompraBuilder cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public CompraBuilder fecha(LocalDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public CompraBuilder agregarElemento(ElementoVenta elemento) {
        this.elementos.add(elemento);
        return this;
    }

    public Compra build() {
        return new Compra(
                id,
                cliente,
                fecha,
                elementos
        );
    }
}
