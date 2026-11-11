package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.*;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ControladorAdministrarSalas {

    @FXML private TableView<Sala> tablaSalas;

    @FXML private TableColumn<Sala, String> colId;
    @FXML private TableColumn<Sala, String> colNombre;
    @FXML private TableColumn<Sala, Integer> colAsientos;

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtFilas;
    @FXML private TextField txtAsientosPorFila;

    private Administrador administradorActual;

    public void configurarAdministrador(Administrador administrador) {

        if (administrador == null) {
            throw new IllegalArgumentException(
                    "Solo un administrador puede gestionar salas."
            );
        }

        administradorActual = administrador;
        cargarSalas();
    }

    @FXML
    private void initialize() {

        colId.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getId()));

        colNombre.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getNombre()));

        colAsientos.setCellValueFactory(d ->
                new ReadOnlyObjectWrapper<>(
                        d.getValue().getAsientos().size()
                ));

        tablaSalas.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {

                    if (seleccionada != null) {

                        txtId.setText(seleccionada.getId());
                        txtNombre.setText(seleccionada.getNombre());

                        txtId.setEditable(false);

                        txtFilas.clear();
                        txtAsientosPorFila.clear();

                        txtFilas.setDisable(true);
                        txtAsientosPorFila.setDisable(true);
                    }
                });
    }

    private void cargarSalas() {

        tablaSalas.getItems().setAll(
                Lanzador.gestorCartelera.consultarSalas()
        );

        tablaSalas.refresh();
    }

    @FXML
    private void registrarSala() {

        if (administradorActual == null) return;

        try {

            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();

            if (id.isBlank() || nombre.isBlank()) {
                throw new IllegalArgumentException(
                        "Debes ingresar el ID y nombre de la sala."
                );
            }

            int filas = Integer.parseInt(
                    txtFilas.getText().trim()
            );

            int asientosPorFila = Integer.parseInt(
                    txtAsientosPorFila.getText().trim()
            );

            Sala sala = new Sala(id, nombre);

            sala.generarAsientos(filas, asientosPorFila);

            Lanzador.gestorCartelera.agregarSala(sala);

            cargarSalas();
            limpiarCampos();

            mostrarMensaje("Sala registrada correctamente.");

        } catch (NumberFormatException e) {
            mostrarMensaje(
                    "Las filas y los asientos deben ser números enteros."
            );

        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void actualizarSala() {

        if (administradorActual == null) return;

        Sala seleccionada =
                tablaSalas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una sala.");
            return;
        }

        try {

            boolean actualizada =
                    Lanzador.gestorCartelera.actualizarSala(
                            seleccionada.getId(),
                            txtNombre.getText().trim()
                    );

            if (actualizada) {
                cargarSalas();
                limpiarCampos();
                mostrarMensaje("Sala actualizada correctamente.");
            }

        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void eliminarSala() {

        if (administradorActual == null) return;

        Sala seleccionada =
                tablaSalas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una sala.");
            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Deseas eliminar la sala " +
                        seleccionada.getNombre() + "?",
                ButtonType.YES,
                ButtonType.NO
        );

        if (confirmacion.showAndWait().orElse(ButtonType.NO)
                != ButtonType.YES) {
            return;
        }

        try {

            boolean eliminada =
                    Lanzador.gestorCartelera.eliminarSala(
                            seleccionada.getId()
                    );

            if (eliminada) {
                cargarSalas();
                limpiarCampos();
                mostrarMensaje("Sala eliminada correctamente.");
            }

        } catch (IllegalStateException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void verAsientos() {

        Sala seleccionada =
                tablaSalas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una sala.");
            return;
        }

        StringBuilder contenido = new StringBuilder();

        contenido.append("Sala: ")
                .append(seleccionada.getNombre())
                .append("\n\n");

        for (Asiento asiento : seleccionada.getAsientos()) {

            contenido.append(asiento.getFila())
                    .append(asiento.getNumero())
                    .append("   ");
        }

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Asientos de la sala");
        alerta.setHeaderText(seleccionada.getNombre());

        TextArea area = new TextArea(contenido.toString());
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefWidth(450);
        area.setPrefHeight(250);

        alerta.getDialogPane().setContent(area);
        alerta.showAndWait();
    }

    @FXML
    private void limpiarCampos() {

        tablaSalas.getSelectionModel().clearSelection();

        txtId.clear();
        txtNombre.clear();
        txtFilas.clear();
        txtAsientosPorFila.clear();

        txtId.setEditable(true);

        txtFilas.setDisable(false);
        txtAsientosPorFila.setDisable(false);
    }

    private void mostrarMensaje(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("CinemaUQ");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void volver() {

        Stage ventana = (Stage) tablaSalas
                .getScene().getWindow();

        ventana.close();
    }
}
