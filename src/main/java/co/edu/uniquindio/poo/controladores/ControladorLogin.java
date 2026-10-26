
package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.Usuario;
import co.edu.uniquindio.poo.modelo.Cliente;
import co.edu.uniquindio.poo.modelo.Administrador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorLogin {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    @FXML
    private void iniciarSesion() {

        String correo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText(
                    "Ingresa tu correo y contraseña."
            );
            return;
        }

        Usuario usuario = Lanzador.gestorUsuarios
                .iniciarSesion(correo, contrasena);

        if (usuario == null) {
            lblMensaje.setText(
                    "Correo o contraseña incorrectos."
            );
            return;
        }

        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource(
                            "/vista/principal.fxml"
                    )
            );

            Scene escena = new Scene(
                    cargador.load(), 1000, 650
            );

            ControladorPrincipal controlador =
                    cargador.getController();

            if (usuario instanceof Cliente) {
                controlador.configurarUsuario(usuario, "CLIENTE");
            } else if (usuario instanceof Administrador) {
                controlador.configurarUsuario(usuario, "ADMINISTRADOR");
            }

            Stage escenario = (Stage) txtCorreo
                    .getScene().getWindow();

            escenario.setTitle(
                    "CinemaUQ - " + usuario.getNombre()
            );
            escenario.setScene(escena);
            escenario.centerOnScreen();

        } catch (IOException e) {
            lblMensaje.setText(
                    "No se pudo abrir la pantalla principal."
            );
            e.printStackTrace();
        }
    }
}

