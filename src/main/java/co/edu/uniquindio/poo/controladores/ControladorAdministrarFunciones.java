package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.*;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ControladorAdministrarFunciones {

    @FXML private TableView<Funcion> tablaFunciones;

    @FXML private TableColumn<Funcion, String> colId;
    @FXML private TableColumn<Funcion, String> colPelicula;
    @FXML private TableColumn<Funcion, String> colSala;
    @FXML private TableColumn<Funcion, String> colFecha;
    @FXML private TableColumn<Funcion, Integer> colReservados;

    @FXML private TextField txtId;
    @FXML private TextField txtHora;

    @FXML private ComboBox<Pelicula> comboPeliculas;
    @FXML private ComboBox<Sala> comboSalas;

    @FXML private DatePicker fechaFuncion;

    private Administrador administradorActual;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void configurarAdministrador(Administrador administrador) {

        if (administrador == null) {
            throw new IllegalArgumentException(
                    "Solo un administrador puede gestionar funciones."
            );
        }

        administradorActual = administrador;

        cargarDatos();
    }

    @FXML
    private void initialize() {

        colId.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getId()));

        colPelicula.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        d.getValue().getPelicula().getTitulo()));

        colSala.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        d.getValue().getSala().getNombre()));

        colFecha.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        d.getValue().getFechaHora().format(formatoFecha)));

        colReservados.setCellValueFactory(d ->
                new ReadOnlyObjectWrapper<>(
                        Lanzador.gestorAsientos.contarAsientosReservados(
                                d.getValue()
                        )
                ));

        comboPeliculas.setConverter(new StringConverter<>() {

            @Override
            public String toString(Pelicula pelicula) {
                return pelicula == null ? "" : pelicula.getTitulo();
            }

            @Override
            public Pelicula fromString(String texto) {
                return null;
            }
        });

        comboSalas.setConverter(new StringConverter<>() {

            @Override
            public String toString(Sala sala) {
                return sala == null ? "" : sala.getNombre();
            }

            @Override
            public Sala fromString(String texto) {
                return null;
            }
        });

        tablaFunciones.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {

                    if (seleccionada != null) {

                        txtId.setText(seleccionada.getId());
                        txtId.setEditable(false);

                        comboPeliculas.setValue(
                                seleccionada.getPelicula());

                        comboSalas.setValue(
                                seleccionada.getSala());

                        fechaFuncion.setValue(
                                seleccionada.getFechaHora().toLocalDate());

                        txtHora.setText(
                                seleccionada.getFechaHora()
                                        .toLocalTime()
                                        .format(
                                                DateTimeFormatter.ofPattern("HH:mm")
                                        )
                        );

                        comboPeliculas.setDisable(true);
                        comboSalas.setDisable(true);
                    }
                });
    }

    private void cargarDatos() {

        comboPeliculas.getItems().setAll(
                Lanzador.gestorCartelera.consultarPeliculas()
        );

        comboSalas.getItems().setAll(
                Lanzador.gestorCartelera.consultarSalas()
        );

        tablaFunciones.getItems().setAll(
                Lanzador.gestorCartelera.consultarFunciones()
        );

        tablaFunciones.refresh();
    }

    private LocalDateTime obtenerFechaHora() {

        if (fechaFuncion.getValue() == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una fecha."
            );
        }

        try {
            LocalTime hora = LocalTime.parse(
                    txtHora.getText().trim(),
                    DateTimeFormatter.ofPattern("HH:mm")
            );

            LocalDateTime fechaHora = LocalDateTime.of(
                    fechaFuncion.getValue(),
                    hora
            );

            if (!fechaHora.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException(
                        "La fecha y hora deben ser futuras."
                );
            }

            return fechaHora;

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "La hora debe tener el formato HH:mm. Ejemplo: 18:30."
            );
        }
    }

    @FXML
    private void registrarFuncion() {

        if (administradorActual == null) return;

        try {
            String id = txtId.getText().trim();

            Pelicula pelicula = comboPeliculas.getValue();
            Sala sala = comboSalas.getValue();

            if (id.isBlank() || pelicula == null || sala == null) {
                throw new IllegalArgumentException(
                        "Debes ingresar ID, película y sala."
                );
            }

            LocalDateTime fechaHora = obtenerFechaHora();

            Funcion funcion = new Funcion(
                    id,
                    pelicula,
                    sala,
                    fechaHora
            );

            Lanzador.gestorCartelera.agregarFuncion(funcion);

            cargarDatos();
            limpiarCampos();

            mostrarMensaje("Función registrada correctamente.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void actualizarFuncion() {

        if (administradorActual == null) return;

        Funcion seleccionada =
                tablaFunciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una función.");
            return;
        }

        try {
            LocalDateTime nuevaFechaHora = obtenerFechaHora();

            boolean actualizado =
                    Lanzador.gestorCartelera.actualizarFuncion(
                            seleccionada.getId(),
                            nuevaFechaHora,
                            Lanzador.gestorAsientos
                    );

            if (actualizado) {
                cargarDatos();
                limpiarCampos();
                mostrarMensaje("Horario actualizado correctamente.");
            } else {
                mostrarMensaje("No se encontró la función.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void eliminarFuncion() {

        if (administradorActual == null) return;

        Funcion seleccionada =
                tablaFunciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una función para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Deseas eliminar la función " +
                        seleccionada.getId() + "?",
                ButtonType.YES,
                ButtonType.NO
        );

        if (confirmacion.showAndWait().orElse(ButtonType.NO)
                != ButtonType.YES) {
            return;
        }

        try {
            boolean eliminada =
                    Lanzador.gestorCartelera.eliminarFuncion(
                            seleccionada.getId(),
                            Lanzador.gestorAsientos
                    );

            if (eliminada) {
                cargarDatos();
                limpiarCampos();
                mostrarMensaje("Función eliminada correctamente.");
            } else {
                mostrarMensaje("No se encontró la función.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void limpiarCampos() {

        tablaFunciones.getSelectionModel().clearSelection();

        txtId.clear();
        txtId.setEditable(true);

        comboPeliculas.setDisable(false);
        comboSalas.setDisable(false);

        comboPeliculas.getSelectionModel().clearSelection();
        comboSalas.getSelectionModel().clearSelection();

        fechaFuncion.setValue(null);
        txtHora.clear();
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
        Stage ventana = (Stage) tablaFunciones
                .getScene().getWindow();

        ventana.close();
    }
}
