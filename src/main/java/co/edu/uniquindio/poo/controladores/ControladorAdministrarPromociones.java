package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.Administrador;
import co.edu.uniquindio.poo.modelo.Promocion;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ControladorAdministrarPromociones {

    @FXML private TableView<Promocion> tablaPromociones;
    @FXML private TableColumn<Promocion, String> colId;
    @FXML private TableColumn<Promocion, String> colNombre;
    @FXML private TableColumn<Promocion, String> colPorcentaje;
    @FXML private TableColumn<Promocion, String> colEstado;

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPorcentaje;

    private Administrador administradorActual;

    @FXML
    private void initialize() {

        colId.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getId()));

        colNombre.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getNombre()));

        colPorcentaje.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        d.getValue().getPorcentajeDescuento() + "%"
                ));

        colEstado.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        d.getValue().isActiva() ? "Activa" : "Inactiva"
                ));

        tablaPromociones.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {

                    if (seleccionada != null) {
                        txtId.setText(seleccionada.getId());
                        txtNombre.setText(seleccionada.getNombre());
                        txtPorcentaje.setText(
                                String.valueOf(
                                        seleccionada.getPorcentajeDescuento()
                                )
                        );
                        txtId.setEditable(false);
                    }
                });
    }

    public void configurarAdministrador(Administrador administrador) {

        if (administrador == null) {
            throw new IllegalArgumentException(
                    "Solo un administrador puede gestionar promociones."
            );
        }

        this.administradorActual = administrador;
        cargarPromociones();
    }

    private void cargarPromociones() {

        tablaPromociones.getItems().setAll(
                Lanzador.gestorPromociones.consultarPromociones()
        );

        tablaPromociones.refresh();
    }

    @FXML
    private void registrarPromocion() {

        if (administradorActual == null) return;

        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            double porcentaje = Double.parseDouble(
                    txtPorcentaje.getText().trim()
            );

            Promocion promocion =
                    new Promocion(id, nombre, porcentaje);

            Lanzador.gestorPromociones.agregarPromocion(promocion);

            cargarPromociones();
            limpiarCampos();
            mostrarMensaje("Promoción registrada correctamente.");

        } catch (NumberFormatException e) {
            mostrarMensaje("Ingresa un porcentaje numérico válido.");

        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void actualizarPromocion() {

        if (administradorActual == null) return;

        Promocion seleccionada =
                tablaPromociones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una promoción.");
            return;
        }

        try {
            double porcentaje = Double.parseDouble(
                    txtPorcentaje.getText().trim()
            );

            Lanzador.gestorPromociones.actualizarPromocion(
                    seleccionada.getId(),
                    txtNombre.getText().trim(),
                    porcentaje
            );

            cargarPromociones();
            limpiarCampos();
            mostrarMensaje("Promoción actualizada correctamente.");

        } catch (NumberFormatException e) {
            mostrarMensaje("Ingresa un porcentaje válido.");

        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void activarPromocion() {

        if (administradorActual == null) return;

        Promocion seleccionada =
                tablaPromociones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una promoción.");
            return;
        }

        Lanzador.gestorPromociones.activarPromocion(
                seleccionada.getId()
        );

        cargarPromociones();
        mostrarMensaje("Promoción activada.");
    }

    @FXML
    private void desactivarPromocion() {

        if (administradorActual == null) return;

        Promocion seleccionada =
                tablaPromociones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una promoción.");
            return;
        }

        Lanzador.gestorPromociones.desactivarPromocion(
                seleccionada.getId()
        );

        cargarPromociones();
        mostrarMensaje("Promoción desactivada.");
    }

    @FXML
    private void eliminarPromocion() {

        if (administradorActual == null) return;

        Promocion seleccionada =
                tablaPromociones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una promoción.");
            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Deseas eliminar la promoción "
                        + seleccionada.getNombre() + "?",
                ButtonType.YES,
                ButtonType.NO
        );

        if (confirmacion.showAndWait().orElse(ButtonType.NO)
                != ButtonType.YES) {
            return;
        }

        Lanzador.gestorPromociones.eliminarPromocion(
                seleccionada.getId()
        );

        cargarPromociones();
        limpiarCampos();
        mostrarMensaje("Promoción eliminada.");
    }

    @FXML
    private void limpiarCampos() {

        tablaPromociones.getSelectionModel().clearSelection();

        txtId.clear();
        txtNombre.clear();
        txtPorcentaje.clear();

        txtId.setEditable(true);
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

        Stage ventana = (Stage) tablaPromociones
                .getScene().getWindow();

        ventana.close();
    }
}
