package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.Administrador;
import co.edu.uniquindio.poo.modelo.Pelicula;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ControladorAdministrarPeliculas {

    @FXML private TableView<Pelicula> tablaPeliculas;

    @FXML private TableColumn<Pelicula, String> colId;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colGenero;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, String> colClasificacion;

    @FXML private TextField txtId;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtGenero;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtClasificacion;

    private Administrador administradorActual;

    public void configurarAdministrador(Administrador administrador) {
        if (administrador == null) {
            throw new IllegalArgumentException(
                    "Solo un administrador puede gestionar películas."
            );
        }

        this.administradorActual = administrador;
        cargarPeliculas();
    }

    @FXML
    private void initialize() {

        colId.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getId()));

        colTitulo.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getTitulo()));

        colGenero.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getGenero()));

        colDuracion.setCellValueFactory(d ->
                new ReadOnlyObjectWrapper<>(
                        d.getValue().getDuracionMinutos()));

        colClasificacion.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        d.getValue().getClasificacion()));

        tablaPeliculas.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {

                    if (seleccionada != null) {
                        txtId.setText(seleccionada.getId());
                        txtId.setEditable(false);

                        txtTitulo.setText(seleccionada.getTitulo());
                        txtGenero.setText(seleccionada.getGenero());
                        txtDuracion.setText(
                                String.valueOf(
                                        seleccionada.getDuracionMinutos()));
                        txtClasificacion.setText(
                                seleccionada.getClasificacion());
                    }
                });
    }

    private void cargarPeliculas() {
        tablaPeliculas.getItems().setAll(
                Lanzador.gestorCartelera.consultarPeliculas()
        );
        tablaPeliculas.refresh();
    }

    @FXML
    private void registrarPelicula() {

        if (administradorActual == null) return;

        try {
            String id = txtId.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String genero = txtGenero.getText().trim();
            String clasificacion = txtClasificacion.getText().trim();
            int duracion = Integer.parseInt(
                    txtDuracion.getText().trim());

            validarDatos(id, titulo, genero, duracion, clasificacion);

            Pelicula pelicula = new Pelicula(
                    id, titulo, genero, duracion, clasificacion
            );

            Lanzador.gestorCartelera.agregarPelicula(pelicula);

            cargarPeliculas();
            limpiarCampos();
            mostrarMensaje("Película registrada correctamente.");

        } catch (NumberFormatException e) {
            mostrarMensaje("La duración debe ser un número entero.");
        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void actualizarPelicula() {

        if (administradorActual == null) return;

        try {
            String id = txtId.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String genero = txtGenero.getText().trim();
            String clasificacion = txtClasificacion.getText().trim();
            int duracion = Integer.parseInt(
                    txtDuracion.getText().trim());

            validarDatos(id, titulo, genero, duracion, clasificacion);

            boolean actualizado =
                    Lanzador.gestorCartelera.actualizarPelicula(
                            id, titulo, genero, duracion, clasificacion
                    );

            if (actualizado) {
                cargarPeliculas();
                limpiarCampos();
                mostrarMensaje("Película actualizada correctamente.");
            } else {
                mostrarMensaje("No se encontró la película.");
            }

        } catch (NumberFormatException e) {
            mostrarMensaje("La duración debe ser un número entero.");
        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void eliminarPelicula() {

        if (administradorActual == null) return;

        Pelicula seleccionada =
                tablaPeliculas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una película para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Deseas eliminar la película "
                        + seleccionada.getTitulo() + "?",
                ButtonType.YES,
                ButtonType.NO
        );

        if (confirmacion.showAndWait().orElse(ButtonType.NO)
                != ButtonType.YES) {
            return;
        }

        try {
            boolean eliminada =
                    Lanzador.gestorCartelera.eliminarPelicula(
                            seleccionada.getId()
                    );

            if (eliminada) {
                cargarPeliculas();
                limpiarCampos();
                mostrarMensaje("Película eliminada correctamente.");
            } else {
                mostrarMensaje("No se encontró la película.");
            }

        } catch (IllegalStateException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void limpiarCampos() {
        tablaPeliculas.getSelectionModel().clearSelection();

        txtId.clear();
        txtId.setEditable(true);

        txtTitulo.clear();
        txtGenero.clear();
        txtDuracion.clear();
        txtClasificacion.clear();
    }

    private void validarDatos(
            String id,
            String titulo,
            String genero,
            int duracion,
            String clasificacion) {

        if (id.isBlank() || titulo.isBlank()
                || genero.isBlank() || clasificacion.isBlank()) {
            throw new IllegalArgumentException(
                    "Todos los campos son obligatorios."
            );
        }

        if (duracion <= 0) {
            throw new IllegalArgumentException(
                    "La duración debe ser mayor que cero."
            );
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
    private void volver() {
        Stage ventana = (Stage) tablaPeliculas
                .getScene().getWindow();
        ventana.close();
    }
}