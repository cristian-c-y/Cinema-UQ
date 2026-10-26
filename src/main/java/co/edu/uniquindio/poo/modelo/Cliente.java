package co.edu.uniquindio.poo.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private CuentaPuntos cuentaPuntos;
    private TarjetaVirtual tarjetaVirtual;
    private List<Compra> compras;

    public Cliente(String id, String nombre,
                   String correo, String contrasena) {
        super(id, nombre, correo, contrasena);

        this.cuentaPuntos = new CuentaPuntos();
        this.tarjetaVirtual = new TarjetaVirtual();
        this.compras = new ArrayList<>();
    }

    public CuentaPuntos getCuentaPuntos() {
        return cuentaPuntos;
    }

    public TarjetaVirtual getTarjetaVirtual() {
        return tarjetaVirtual;
    }

    public List<Compra> getCompras() {
        return List.copyOf(compras);
    }

    public void agregarCompra(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException(
                    "La compra no puede ser nula."
            );
        }

        compras.add(compra);
    }
}
