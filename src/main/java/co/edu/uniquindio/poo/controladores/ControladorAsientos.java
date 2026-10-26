package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import co.edu.uniquindio.poo.patrones.creacionales.CompraBuilder;
import co.edu.uniquindio.poo.patrones.creacionales.CreadorEntrada;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;
import co.edu.uniquindio.poo.patrones.estructurales.CompraFacade;

import java.time.LocalDateTime;
import java.util.UUID;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ControladorAsientos {

    @FXML private Label lblInformacion;
    @FXML private Label lblSeleccion;
    @FXML private ComboBox<Pelicula> cbPelicula;
    @FXML private ComboBox<Funcion> cbFuncion;
    @FXML private GridPane gridAsientos;
    @FXML private Label lblTotal;

    private Cliente clienteActual;

    private final List<Asiento> asientosSeleccionados =
            new ArrayList<>();

    private final DateTimeFormatter formato =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final double PRECIO_ENTRADA = 18000;

    @FXML
    private void initialize() {

        cbPelicula.getItems().setAll(
                Lanzador.gestorCartelera.consultarPeliculas()
        );

        cbPelicula.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(Pelicula pelicula, boolean empty) {
                super.updateItem(pelicula, empty);

                setText(empty || pelicula == null
                        ? null : pelicula.getTitulo());
            }
        });

        cbPelicula.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pelicula pelicula, boolean empty) {
                super.updateItem(pelicula, empty);

                setText(empty || pelicula == null
                        ? null : pelicula.getTitulo());
            }
        });

        cbFuncion.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(Funcion funcion, boolean empty) {
                super.updateItem(funcion, empty);

                setText(empty || funcion == null
                        ? null : describirFuncion(funcion));
            }
        });

        cbFuncion.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Funcion funcion, boolean empty) {
                super.updateItem(funcion, empty);

                setText(empty || funcion == null
                        ? null : describirFuncion(funcion));
            }
        });
    }

    public void configurarCliente(Cliente cliente) {
        this.clienteActual = cliente;

        lblInformacion.setText(
                "Cliente: " + cliente.getNombre()
        );
    }

    private String describirFuncion(Funcion funcion) {
        return funcion.getFechaHora().format(formato)
                + " - " + funcion.getSala().getNombre();
    }

    @FXML
    private void seleccionarPelicula() {

        Pelicula pelicula = cbPelicula.getValue();

        cbFuncion.getItems().clear();
        cbFuncion.setValue(null);

        gridAsientos.getChildren().clear();
        asientosSeleccionados.clear();

        actualizarSeleccion();

        if (pelicula != null) {
            cbFuncion.getItems().setAll(
                    Lanzador.gestorCartelera
                            .consultarFuncionesPorPelicula(
                                    pelicula.getId())
            );
        }
    }

    @FXML
    private void seleccionarFuncion() {

        gridAsientos.getChildren().clear();
        asientosSeleccionados.clear();

        actualizarSeleccion();

        Funcion funcion = cbFuncion.getValue();

        if (funcion == null) {
            return;
        }

        int columna = 0;
        int fila = 0;

        for (Asiento asiento : funcion.getSala().getAsientos()) {

            Button boton = new Button(
                    asiento.getFila() + asiento.getNumero()
            );

            boton.setPrefSize(55, 45);

            boolean disponible =
                    Lanzador.gestorAsientos
                            .asientoDisponible(funcion, asiento);

            if (disponible) {

                boton.setStyle(
                        "-fx-background-color: #22c55e;"
                                + "-fx-text-fill: white;"
                );

                boton.setOnAction(evento ->
                        seleccionarAsiento(asiento, boton));

            } else {

                boton.setDisable(true);

                boton.setStyle(
                        "-fx-background-color: #ef4444;"
                                + "-fx-text-fill: white;"
                );
            }

            gridAsientos.add(boton, columna, fila);

            columna++;

            if (columna == 8) {
                columna = 0;
                fila++;
            }
        }
    }

    private void seleccionarAsiento(Asiento asiento, Button boton) {

        if (asientosSeleccionados.contains(asiento)) {

            asientosSeleccionados.remove(asiento);

            boton.setStyle(
                    "-fx-background-color: #22c55e;"
                            + "-fx-text-fill: white;"
            );

        } else {

            asientosSeleccionados.add(asiento);

            boton.setStyle(
                    "-fx-background-color: #3b82f6;"
                            + "-fx-text-fill: white;"
            );
        }

        actualizarSeleccion();
    }

    private void actualizarSeleccion() {

        StringBuilder texto = new StringBuilder(
                "Asientos seleccionados: "
        );

        if (asientosSeleccionados.isEmpty()) {
            texto.append("Ninguno");
        } else {
            for (Asiento asiento : asientosSeleccionados) {
                texto.append(asiento.getFila())
                        .append(asiento.getNumero())
                        .append(" ");
            }
        }

        lblSeleccion.setText(texto.toString());

        double total = asientosSeleccionados.size() * PRECIO_ENTRADA;

        lblTotal.setText(
                "Total a pagar: $" + String.format("%,.0f", total)
        );
    }

    @FXML
    private void continuarCompra() {

        Funcion funcion = cbFuncion.getValue();

        if (clienteActual == null || funcion == null
                || asientosSeleccionados.isEmpty()) {

            mostrarAlerta(
                    "Debes seleccionar una función y al menos un asiento."
            );
            return;
        }

        List<Asiento> asientosCompra =
                new ArrayList<>(asientosSeleccionados);

        for (Asiento asiento : asientosCompra) {
            if (!Lanzador.gestorAsientos
                    .asientoDisponible(funcion, asiento)) {

                mostrarAlerta(
                        "Uno de los asientos seleccionados ya está ocupado."
                );
                seleccionarFuncion();
                return;
            }
        }

        CompraBuilder builder = new CompraBuilder()
                .id(UUID.randomUUID().toString())
                .cliente(clienteActual)
                .fecha(LocalDateTime.now());

        for (Asiento asiento : asientosCompra) {

            ElementoVenta entrada = new CreadorEntrada(
                    UUID.randomUUID().toString(),
                    funcion,
                    asiento,
                    PRECIO_ENTRADA
            ).crearElemento();

            builder.agregarElemento(entrada);
        }

        Compra compra = builder.build();

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmacion.setTitle("Confirmar compra");
        confirmacion.setHeaderText("Resumen de compra");

        confirmacion.setContentText(
                "Película: " + funcion.getPelicula().getTitulo()
                        + "\nFunción: " + describirFuncion(funcion)
                        + "\nEntradas: " + asientosCompra.size()
                        + "\nTotal: $" + String.format("%,.0f", compra.getTotal())
                        + "\n\n¿Deseas continuar con el pago?"
        );

        var respuesta = confirmacion.showAndWait();

        if (respuesta.isEmpty()
                || respuesta.get() != ButtonType.OK) {
            return;
        }

        TextInputDialog dialogo = new TextInputDialog();

        dialogo.setTitle("Pago CinemaUQ");
        dialogo.setHeaderText("Número de tarjeta");
        dialogo.setContentText("Ingresa el número de tarjeta:");

        var numeroIngresado = dialogo.showAndWait();

        if (numeroIngresado.isEmpty()
                || numeroIngresado.get().isBlank()) {
            return;
        }

        String numeroTarjeta = numeroIngresado.get().trim();

        CompraFacade facade = Lanzador.compraFacade;

        boolean realizada;

        try {
            realizada = facade.realizarCompraMultiple(
                    clienteActual,
                    compra,
                    funcion,
                    asientosCompra,
                    numeroTarjeta
            );
        } catch (RuntimeException e) {
            mostrarAlerta("No se pudo procesar la compra: "
                    + e.getMessage());
            seleccionarFuncion();
            return;
        }

        if (realizada) {
            mostrarAlerta(
                    "¡Compra realizada correctamente!"
                            + "\nTotal: $" + String.format("%,.0f", compra.getTotal())
                            + "\nEntradas: " + asientosCompra.size()
            );
        } else {
            mostrarAlerta(
                    "No fue posible completar la compra."
                            + "\nVerifica el saldo, la tarjeta y la disponibilidad."
            );
        }

        seleccionarFuncion();
    }

    private void mostrarAlerta(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("CinemaUQ");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

    @FXML
    private void volverMenu() {

        try {

            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vista/principal.fxml")
            );

            Scene escena = new Scene(cargador.load(), 1000, 650);

            ControladorPrincipal controlador =
                    cargador.getController();

            controlador.configurarUsuario(
                    clienteActual, "CLIENTE"
            );

            Stage escenario = (Stage)
                    lblInformacion.getScene().getWindow();

            escenario.setScene(escena);
            escenario.setTitle("CinemaUQ - Principal");
            escenario.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo regresar al menú.");
        }
    }

}