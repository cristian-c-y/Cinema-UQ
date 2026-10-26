package co.edu.uniquindio.poo.modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

    private final List<Usuario> usuarios;

    public GestorUsuarios() {
        usuarios = new ArrayList<>();
    }

    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario no puede ser nulo."
            );
        }

        boolean correoExiste = usuarios.stream()
                .anyMatch(u -> u.getCorreo()
                        .equalsIgnoreCase(usuario.getCorreo()));

        if (correoExiste) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con ese correo."
            );
        }

        usuarios.add(usuario);
    }

    public Usuario iniciarSesion(String correo, String contrasena) {
        if (correo == null || contrasena == null) {
            return null;
        }

        return usuarios.stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo.trim()))
                .filter(u -> u.getContrasena().equals(contrasena))
                .findFirst()
                .orElse(null);
    }

    public List<Usuario> getUsuarios() {
        return List.copyOf(usuarios);
    }
}
