package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.modelo.Usuario;

import javafx.fxml.FXML;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.control.Button;

import java.io.IOException;

public class ControladorPrincipal {

    @FXML private Button btnRecargarTarjeta;
    @FXML private Button btnAdministrarPeliculas;
    @FXML private Button btnAdministrarFunciones;
    @FXML private Button btnAdministrarSalas;
    @FXML private Button btnAdministrarConfiteria;
    @FXML private Button btnAdministrarPromociones;

    @FXML private Button btnConfiteria;
    @FXML private Button btnMisCompras;
    @FXML private Button btnMiTarjeta;
    @FXML private Button btnSeleccionarAsientos;

    private Cliente buscarClientePorCorreo(String correo) {

        for (Usuario usuario : Lanzador.gestorUsuarios.getUsuarios()) {

            if (usuario instanceof Cliente cliente
                    && cliente.getCorreo().equalsIgnoreCase(correo.trim())) {

                return cliente;
            }
        }

        return null;
    }

    @FXML
    private Label lblSeccion;

    private Usuario usuarioActual;
    private String rolActual;

    public void configurarUsuario(Usuario usuario, String rol) {

        this.usuarioActual = usuario;
        this.rolActual = rol;

        lblSeccion.setText(
                "¡Bienvenido a CinemaUQ, " + usuario.getNombre() + "!"
                        + "\n\nRol: " + rolActual
                        + "\n\nSelecciona una opción del menú."
        );

        boolean esAdministrador = usuario instanceof Administrador;
        boolean esCliente = usuario instanceof Cliente;

        // Opciones exclusivas del administrador
        btnRecargarTarjeta.setVisible(esAdministrador);
        btnRecargarTarjeta.setManaged(esAdministrador);

        btnAdministrarPeliculas.setVisible(esAdministrador);
        btnAdministrarPeliculas.setManaged(esAdministrador);

        btnAdministrarFunciones.setVisible(esAdministrador);
        btnAdministrarFunciones.setManaged(esAdministrador);

        btnAdministrarSalas.setVisible(esAdministrador);
        btnAdministrarSalas.setManaged(esAdministrador);

        btnAdministrarConfiteria.setVisible(esAdministrador);
        btnAdministrarConfiteria.setManaged(esAdministrador);

        btnAdministrarPromociones.setVisible(esAdministrador);
        btnAdministrarPromociones.setManaged(esAdministrador);

        // Opciones exclusivas del cliente
        btnConfiteria.setVisible(esCliente);
        btnConfiteria.setManaged(esCliente);

        btnMisCompras.setVisible(esCliente);
        btnMisCompras.setManaged(esCliente);

        btnMiTarjeta.setVisible(esCliente);
        btnMiTarjeta.setManaged(esCliente);

        btnSeleccionarAsientos.setVisible(esCliente);
        btnSeleccionarAsientos.setManaged(esCliente);
    }

    @FXML
    private void mostrarCartelera() {

        StringBuilder contenido = new StringBuilder();

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        contenido.append("CARTELERA CINEMAUQ\n\n");

        for (Pelicula pelicula :
                Lanzador.gestorCartelera.consultarPeliculas()) {

            contenido.append("PELÍCULA: ")
                    .append(pelicula.getTitulo())
                    .append("\n");

            contenido.append("Género: ")
                    .append(pelicula.getGenero())
                    .append("\n");

            contenido.append("Duración: ")
                    .append(pelicula.getDuracionMinutos())
                    .append(" minutos\n");

            contenido.append("Clasificación: ")
                    .append(pelicula.getClasificacion())
                    .append("\n");

            contenido.append("FUNCIONES DISPONIBLES:\n");

            List<Funcion> funciones =
                    Lanzador.gestorCartelera
                            .consultarFuncionesPorPelicula(
                                    pelicula.getId());

            if (funciones.isEmpty()) {
                contenido.append("No hay funciones programadas.\n");
            } else {

                for (Funcion funcion : funciones) {

                    contenido.append("  - ")
                            .append(funcion.getFechaHora().format(formato))
                            .append(" | ")
                            .append(funcion.getSala().getNombre())
                            .append("\n");
                }
            }

            contenido.append("\n---------------------------\n\n");
        }

        lblSeccion.setText(contenido.toString());
    }

    @FXML
    private void mostrarConfiteria() {

        if (!(usuarioActual instanceof Cliente cliente)) {
            mostrarMensaje("La compra de confitería está disponible para clientes.");
            return;
        }

        try {

            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/confiteria.fxml")
            );

            Scene escena = new Scene(cargador.load(), 950, 650);

            ControladorConfiteria controlador = cargador.getController();
            controlador.configurarCliente(cliente);

            Stage ventana = new Stage();
            ventana.setTitle("CinemaUQ - Confitería");
            ventana.setScene(escena);
            ventana.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();

            mostrarMensaje(
                    "No se pudo abrir Confitería:\n"
                            + e.getMessage()
            );
        }
    }

    @FXML
    private void mostrarCompras() {

        if (!(usuarioActual instanceof Cliente cliente)) {
            mostrarMensaje(
                    "El historial de compras está disponible para clientes."
            );
            return;
        }

        try {

            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/compras.fxml")
            );

            Scene escena = new Scene(cargador.load(), 950, 650);

            ControladorCompras controlador = cargador.getController();
            controlador.configurarCliente(cliente);

            Stage ventana = new Stage();
            ventana.setTitle("CinemaUQ - Historial de compras");
            ventana.setScene(escena);
            ventana.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();

            mostrarMensaje(
                    "No se pudo abrir el historial:\n"
                            + e.getMessage()
            );
        }
    }
    @FXML
    private void mostrarTarjeta() {

        if (!(usuarioActual instanceof Cliente cliente)) {
            mostrarMensaje(
                    "La consulta de tarjeta virtual está disponible para clientes."
            );
            return;
        }

        try {

            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/tarjeta.fxml")
            );

            Scene escena = new Scene(cargador.load(), 900, 600);

            ControladorTarjeta controlador = cargador.getController();
            controlador.configurarCliente(cliente);

            Stage ventana = new Stage();
            ventana.setTitle("CinemaUQ - Mi tarjeta virtual");
            ventana.setScene(escena);
            ventana.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();

            mostrarMensaje(
                    "No se pudo abrir la tarjeta virtual:\n"
                            + e.getMessage()
            );
        }
    }

    @FXML
    private void recargarTarjeta() {

        if (!(usuarioActual instanceof Administrador administrador)) {
            mostrarMensaje(
                    "Solo un administrador puede autorizar recargas."
            );
            return;
        }

        TextInputDialog dialogoCliente = new TextInputDialog();

        dialogoCliente.setTitle("Recargar tarjeta");
        dialogoCliente.setHeaderText("Buscar cliente");
        dialogoCliente.setContentText(
                "Ingresa el correo del cliente:"
        );

        Optional<String> correo = dialogoCliente.showAndWait();

        if (correo.isEmpty() || correo.get().isBlank()) {
            return;
        }

        Cliente cliente = buscarClientePorCorreo(correo.get().trim());

        if (cliente == null) {
            mostrarMensaje("No se encontró el cliente.");
            return;
        }

        TextInputDialog dialogoMonto = new TextInputDialog();

        dialogoMonto.setTitle("Recargar tarjeta");
        dialogoMonto.setHeaderText(
                "Cliente: " + cliente.getNombre()
        );
        dialogoMonto.setContentText("Valor de la recarga:");

        Optional<String> montoIngresado = dialogoMonto.showAndWait();

        if (montoIngresado.isEmpty()) {
            return;
        }

        try {
            double monto = Double.parseDouble(
                    montoIngresado.get().trim()
            );

            if (!Double.isFinite(monto) || monto <= 0) {
                mostrarMensaje("Ingresa un valor positivo y válido.");
                return;
            }

            cliente.getTarjetaVirtual().recargar(
                    monto, administrador
            );

            mostrarMensaje(
                    "Recarga realizada correctamente."
                            + "\nCliente: " + cliente.getNombre()
                            + "\nValor: $" + String.format("%,.0f", monto)
                            + "\nNuevo saldo: $"
                            + String.format("%,.0f",
                            cliente.getTarjetaVirtual().getSaldo())
            );

        } catch (NumberFormatException e) {
            mostrarMensaje("Debes ingresar un número válido.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    private void mostrarMensaje(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("CinemaUQ");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void seleccionarAsientos() {

        if (!(usuarioActual instanceof Cliente)) {
            lblSeccion.setText(
                    "La selección de asientos está disponible para clientes."
            );
            return;
        }

        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/asientos.fxml")
            );

            Scene escena = new Scene(cargador.load(), 1000, 650);

            ControladorAsientos controlador = cargador.getController();

            controlador.configurarCliente((Cliente) usuarioActual);

            Stage escenario = (Stage) lblSeccion
                    .getScene().getWindow();

            escenario.setTitle("CinemaUQ - Selección de asientos");
            escenario.setScene(escena);
            escenario.centerOnScreen();

        } catch (IOException e) {
            lblSeccion.setText(
                    "No se pudo abrir la selección de asientos."
            );
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/login.fxml")
            );

            Scene escena = new Scene(cargador.load(), 900, 600);

            Stage escenario = (Stage) lblSeccion
                    .getScene().getWindow();

            escenario.setTitle("CinemaUQ - Iniciar sesión");
            escenario.setScene(escena);
            escenario.centerOnScreen();

        } catch (IOException e) {
            lblSeccion.setText(
                    "No se pudo regresar al inicio de sesión."
            );
            e.printStackTrace();
        }
    }

    @FXML
    private void administrarPeliculas() {

        if (!(usuarioActual instanceof Administrador administrador)) {
            mostrarMensaje("Solo un administrador puede gestionar películas.");
            return;
        }

        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/administrar-peliculas.fxml")
            );

            Scene escena = new Scene(cargador.load(), 950, 650);

            ControladorAdministrarPeliculas controlador =
                    cargador.getController();

            controlador.configurarAdministrador(administrador);

            Stage ventana = new Stage();
            ventana.setTitle("CinemaUQ - Administración de películas");
            ventana.setScene(escena);
            ventana.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(
                    "No se pudo abrir la administración de películas:\n"
                            + e.getMessage()
            );
        }
    }

    @FXML
    private void administrarFunciones() {

        if (!(usuarioActual instanceof Administrador administrador)) {
            mostrarMensaje("Solo un administrador puede gestionar funciones.");
            return;
        }

        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/administrar-funciones.fxml")
            );

            Scene escena = new Scene(cargador.load(), 1000, 650);

            ControladorAdministrarFunciones controlador =
                    cargador.getController();

            controlador.configurarAdministrador(administrador);

            Stage ventana = new Stage();
            ventana.setTitle("CinemaUQ - Administración de funciones");
            ventana.setScene(escena);
            ventana.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(
                    "No se pudo abrir la administración de funciones:\n"
                            + e.getMessage()
            );
        }
    }


    @FXML
    private void administrarSalas() {

        if (!(usuarioActual instanceof Administrador administrador)) {
            mostrarMensaje("Solo un administrador puede gestionar salas.");
            return;
        }

        try {

            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/administrar-salas.fxml")
            );

            Scene escena = new Scene(cargador.load(), 950, 650);

            ControladorAdministrarSalas controlador =
                    cargador.getController();

            controlador.configurarAdministrador(administrador);

            Stage ventana = new Stage();
            ventana.setTitle("CinemaUQ - Administración de salas");
            ventana.setScene(escena);
            ventana.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(
                    "No se pudo abrir la administración de salas:\n"
                            + e.getMessage()
            );
        }
    }


    @FXML
    private void administrarConfiteria() {

        if (!(usuarioActual instanceof Administrador administrador)) {
            mostrarMensaje("Solo un administrador puede gestionar confitería.");
            return;
        }

        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/administrar-confiteria.fxml")
            );

            Scene escena = new Scene(cargador.load(), 1000, 700);

            ControladorAdministrarConfiteria controlador =
                    cargador.getController();

            controlador.configurarAdministrador(administrador);

            Stage ventana = new Stage();
            ventana.setTitle("CinemaUQ - Administración de confitería");
            ventana.setScene(escena);
            ventana.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(
                    "No se pudo abrir la administración de confitería:\n"
                            + e.getMessage()
            );
        }
    }


    @FXML
    private void administrarPromociones() {

        if (!(usuarioActual instanceof Administrador administrador)) {
            mostrarMensaje("Solo un administrador puede gestionar promociones.");
            return;
        }

        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/administrar-promociones.fxml")
            );

            Scene escena = new Scene(cargador.load(), 900, 600);

            ControladorAdministrarPromociones controlador =
                    cargador.getController();

            controlador.configurarAdministrador(administrador);

            Stage ventana = new Stage();
            ventana.setTitle("CinemaUQ - Administración de promociones");
            ventana.setScene(escena);
            ventana.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("No se pudo abrir la administración de promociones.");
        }
    }


}
