package co.edu.uniquindio.poo.modelo;

import co.edu.uniquindio.poo.patrones.estructurales.ElementoConfiteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Combo implements ElementoConfiteria {

    private String id;
    private String nombre;
    private List<ElementoConfiteria> elementos;

    public Combo(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.elementos = new ArrayList<>();
    }

    public void agregarElemento(ElementoConfiteria elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException(
                    "El elemento del combo no puede ser nulo."
            );
        }

        if (elemento == this) {
            throw new IllegalArgumentException(
                    "Un combo no puede contenerse a sí mismo."
            );
        }

        elementos.add(elemento);
    }

    public boolean eliminarElemento(ElementoConfiteria elemento) {
        return elementos.remove(elemento);
    }

    public List<ElementoConfiteria> getElementos() {
        return Collections.unmodifiableList(elementos);
    }

    public String getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double calcularPrecio() {
        return elementos.stream()
                .mapToDouble(ElementoConfiteria::calcularPrecio)
                .sum();
    }
}