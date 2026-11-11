package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.*;
import co.edu.uniquindio.poo.patrones.creacionales.CreadorCombo;
import co.edu.uniquindio.poo.patrones.creacionales.CreadorProducto;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class ControladorAdministrarConfiteria {

    @FXML private TableView<ElementoVenta> tablaCatalogo;

    @FXML private TableColumn<ElementoVenta, String> colId;
    @FXML private TableColumn<ElementoVenta, String> colNombre;
    @FXML private TableColumn<ElementoVenta, String> colTipo;
    @FXML private TableColumn<ElementoVenta, String> colPrecio;

    @FXML private TextField txtIdProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtPrecioProducto;

    @FXML private TextField txtIdCombo;
    @FXML private TextField txtNombreCombo;

    @FXML private ListView<Producto> listaProductosCombo;

    private Administrador administradorActual;

    public void configurarAdministrador(Administrador administrador) {

        if (administrador == null) {
            throw new IllegalArgumentException(
                    "Solo un administrador puede gestionar confitería."
            );
        }

        administradorActual = administrador;
        cargarDatos();
    }

    @FXML
    private void initialize() {

        colId.setCellValueFactory(d -> {
            ElementoVenta e = d.getValue();

            if (e instanceof Producto p) {
                return new ReadOnlyStringWrapper(p.getId());
            }

            if (e instanceof Combo c) {
                return new ReadOnlyStringWrapper(c.getId());
            }

            return new ReadOnlyStringWrapper("");
        });

        colNombre.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        d.getValue().getNombre()
                )
        );

        colTipo.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        d.getValue() instanceof Combo
                                ? "Combo" : "Producto"
                )
        );

        colPrecio.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(
                        "$" + String.format(
                                "%,.0f",
                                d.getValue().calcularPrecio()
                        )
                )
        );

        listaProductosCombo.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);

        listaProductosCombo.setCellFactory(lista -> new ListCell<>() {

            @Override
            protected void updateItem(Producto producto, boolean empty) {
                super.updateItem(producto, empty);

                if (empty || producto == null) {
                    setText(null);
                } else {
                    setText(
                            producto.getNombre()
                                    + " - $"
                                    + String.format("%,.0f", producto.getPrecio())
                    );
                }
            }
        });

        tablaCatalogo.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, seleccionado) -> {

                    if (seleccionado instanceof Producto p) {

                        txtIdProducto.setText(p.getId());
                        txtNombreProducto.setText(p.getNombre());
                        txtPrecioProducto.setText(
                                String.valueOf(p.getPrecio())
                        );

                        txtIdProducto.setEditable(false);

                    } else if (seleccionado instanceof Combo c) {

                        txtIdCombo.setText(c.getId());
                        txtNombreCombo.setText(c.getNombre());

                        txtIdCombo.setEditable(false);
                    }
                });
    }

    private void cargarDatos() {

        List<ElementoVenta> elementos =
                Lanzador.gestorConfiteria.consultarCatalogo();

        tablaCatalogo.getItems().setAll(elementos);

        listaProductosCombo.getItems().setAll(
                elementos.stream()
                        .filter(e -> e instanceof Producto)
                        .map(e -> (Producto) e)
                        .toList()
        );

        tablaCatalogo.refresh();
    }

    @FXML
    private void registrarProducto() {

        if (administradorActual == null) return;

        try {
            String id = txtIdProducto.getText().trim();
            String nombre = txtNombreProducto.getText().trim();

            double precio = Double.parseDouble(
                    txtPrecioProducto.getText().trim()
            );

            if (id.isBlank() || nombre.isBlank()) {
                throw new IllegalArgumentException(
                        "Debes ingresar el ID y nombre del producto."
                );
            }

            if (!Double.isFinite(precio) || precio < 0) {
                throw new IllegalArgumentException(
                        "El precio debe ser válido y no negativo."
                );
            }

            Producto producto = (Producto) new CreadorProducto(
                    id, nombre, precio
            ).crearElemento();

            Lanzador.gestorConfiteria.agregarElemento(producto);

            cargarDatos();
            limpiarCampos();

            mostrarMensaje("Producto registrado correctamente.");

        } catch (NumberFormatException e) {
            mostrarMensaje("Ingresa un precio numérico válido.");

        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void actualizarProducto() {

        if (administradorActual == null) return;

        ElementoVenta seleccionado =
                tablaCatalogo.getSelectionModel().getSelectedItem();

        if (!(seleccionado instanceof Producto producto)) {
            mostrarMensaje("Selecciona un producto de la tabla.");
            return;
        }

        try {
            double precio = Double.parseDouble(
                    txtPrecioProducto.getText().trim()
            );

            boolean actualizado =
                    Lanzador.gestorConfiteria.actualizarProducto(
                            producto.getId(),
                            txtNombreProducto.getText().trim(),
                            precio
                    );

            if (actualizado) {
                cargarDatos();
                limpiarCampos();
                mostrarMensaje("Producto actualizado correctamente.");
            }

        } catch (NumberFormatException e) {
            mostrarMensaje("El precio debe ser numérico.");

        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void registrarCombo() {

        if (administradorActual == null) return;

        try {
            String id = txtIdCombo.getText().trim();
            String nombre = txtNombreCombo.getText().trim();

            if (id.isBlank() || nombre.isBlank()) {
                throw new IllegalArgumentException(
                        "Debes ingresar el ID y nombre del combo."
                );
            }

            List<Producto> seleccionados =
                    List.copyOf(
                            listaProductosCombo.getSelectionModel()
                                    .getSelectedItems()
                    );

            if (seleccionados.isEmpty()) {
                throw new IllegalArgumentException(
                        "Selecciona al menos un producto."
                );
            }

            Combo combo = (Combo) new CreadorCombo(
                    id, nombre
            ).crearElemento();

            for (Producto producto : seleccionados) {
                combo.agregarElemento(producto);
            }

            Lanzador.gestorConfiteria.agregarElemento(combo);

            cargarDatos();
            limpiarCampos();

            mostrarMensaje("Combo registrado correctamente.");

        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void actualizarCombo() {

        if (administradorActual == null) return;

        ElementoVenta seleccionado =
                tablaCatalogo.getSelectionModel().getSelectedItem();

        if (!(seleccionado instanceof Combo combo)) {
            mostrarMensaje("Selecciona un combo de la tabla.");
            return;
        }

        try {
            boolean actualizado =
                    Lanzador.gestorConfiteria.actualizarCombo(
                            combo.getId(),
                            txtNombreCombo.getText().trim()
                    );

            if (actualizado) {
                cargarDatos();
                limpiarCampos();
                mostrarMensaje("Combo actualizado correctamente.");
            }

        } catch (IllegalArgumentException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void eliminarSeleccionado() {

        if (administradorActual == null) return;

        ElementoVenta seleccionado =
                tablaCatalogo.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje("Selecciona un elemento de la tabla.");
            return;
        }

        String id;

        if (seleccionado instanceof Producto p) {
            id = p.getId();
        } else if (seleccionado instanceof Combo c) {
            id = c.getId();
        } else {
            mostrarMensaje("Tipo de elemento no reconocido.");
            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Deseas eliminar " + seleccionado.getNombre() + "?",
                ButtonType.YES,
                ButtonType.NO
        );

        if (confirmacion.showAndWait().orElse(ButtonType.NO)
                != ButtonType.YES) {
            return;
        }

        try {
            boolean eliminado =
                    Lanzador.gestorConfiteria.eliminarElemento(id);

            if (eliminado) {
                cargarDatos();
                limpiarCampos();
                mostrarMensaje("Elemento eliminado correctamente.");
            }

        } catch (IllegalStateException e) {
            mostrarMensaje(e.getMessage());
        }
    }

    @FXML
    private void limpiarCampos() {

        tablaCatalogo.getSelectionModel().clearSelection();
        listaProductosCombo.getSelectionModel().clearSelection();

        txtIdProducto.clear();
        txtNombreProducto.clear();
        txtPrecioProducto.clear();

        txtIdCombo.clear();
        txtNombreCombo.clear();

        txtIdProducto.setEditable(true);
        txtIdCombo.setEditable(true);
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

        Stage ventana = (Stage) tablaCatalogo
                .getScene().getWindow();

        ventana.close();
    }
}
