package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.modelo.Usuario;

import javafx.fxml.FXML;
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

        btnRecargarTarjeta.setVisible(esAdministrador);
        btnRecargarTarjeta.setManaged(esAdministrador);
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

        StringBuilder contenido = new StringBuilder();

        contenido.append("CONFITERÍA CINEMAUQ\n\n");

        var catalogo =
                Lanzador.gestorConfiteria.consultarCatalogo();

        if (catalogo.isEmpty()) {

            contenido.append(
                    "Todavía no hay productos ni combos registrados."
            );

        } else {

            for (var elemento : catalogo) {

                contenido.append("Producto: ")
                        .append(elemento.getNombre())
                        .append("\nPrecio: $")
                        .append(String.format(
                                "%,.0f",
                                elemento.calcularPrecio()
                        ))
                        .append("\n\n----------------------\n\n");
            }
        }

        lblSeccion.setText(contenido.toString());
    }

    @FXML
    private void mostrarCompras() {

        if (usuarioActual == null) {
            lblSeccion.setText("No hay un usuario activo.");
            return;
        }

        if (usuarioActual instanceof Cliente cliente) {

            StringBuilder contenido = new StringBuilder();

            contenido.append("MIS COMPRAS\n\n");

            List<Compra> compras = cliente.getCompras();

            if (compras.isEmpty()) {
                contenido.append("Todavía no has realizado compras.");
            } else {

                DateTimeFormatter formato =
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                for (Compra compra : compras) {

                    contenido.append("Compra: ")
                            .append(compra.getId())
                            .append("\n");

                    contenido.append("Fecha: ")
                            .append(compra.getFecha().format(formato))
                            .append("\n");

                    contenido.append("Productos y entradas:\n");

                    for (var elemento : compra.getElementos()) {

                        contenido.append("  - ")
                                .append(elemento.getNombre())
                                .append(" | $")
                                .append(String.format(
                                        "%,.0f",
                                        elemento.calcularPrecio()
                                ))
                                .append("\n");

                        if (elemento instanceof Entrada entrada) {
                            contenido.append("    Asiento: ")
                                    .append(entrada.getAsiento().getFila())
                                    .append(entrada.getAsiento().getNumero())
                                    .append("\n");
                        }
                    }

                    contenido.append("TOTAL: $")
                            .append(String.format(
                                    "%,.0f",
                                    compra.getTotal()
                            ))
                            .append("\n");

                    contenido.append(
                            "\n-----------------------------\n\n"
                    );
                }
            }

            lblSeccion.setText(contenido.toString());

        } else {

            lblSeccion.setText(
                    "GESTIÓN DE COMPRAS\n\n"
                            + "Aquí el administrador podrá consultar "
                            + "las operaciones del cine."
            );
        }
    }

    @FXML
    private void mostrarTarjeta() {

        if (usuarioActual instanceof Cliente cliente) {

            double saldo = cliente.getTarjetaVirtual().getSaldo();

            int puntos = cliente.getCuentaPuntos().consultarSaldo();

            String contenido =
                    "MI TARJETA VIRTUAL\n\n"
                            + "Cliente: " + cliente.getNombre()
                            + "\n\n"
                            + "Saldo disponible: $"
                            + String.format("%,.0f", saldo)
                            + "\n\n"
                            + "Puntos acumulados: " + puntos
                            + "\n\n"
                            + "Por cada $1.000 en compras acumulas 1 punto."
                            + "\n\n"
                            + "Puedes utilizar tu tarjeta virtual "
                            + "para comprar entradas y productos.";

            lblSeccion.setText(contenido);

        } else {

            lblSeccion.setText(
                    "ADMINISTRACIÓN DE TARJETAS\n\n"
                            + "Aquí podrás consultar las tarjetas "
                            + "virtuales de los clientes."
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
}
