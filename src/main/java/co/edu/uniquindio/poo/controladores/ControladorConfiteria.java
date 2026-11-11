package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.aplicacion.Lanzador;
import co.edu.uniquindio.poo.modelo.*;
import co.edu.uniquindio.poo.patrones.comportamiento.DescuentoPorcentaje;
import co.edu.uniquindio.poo.patrones.comportamiento.EstrategiaDescuento;
import co.edu.uniquindio.poo.patrones.creacionales.CompraBuilder;
import co.edu.uniquindio.poo.patrones.creacionales.GeneradorConsecutivos;
import co.edu.uniquindio.poo.patrones.estructurales.DecoradorMantequilla;
import co.edu.uniquindio.poo.patrones.estructurales.DecoradorQueso;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControladorConfiteria {

    @FXML private TableView<ElementoVenta> tablaProductos;
    @FXML private TableColumn<ElementoVenta, String> colNombre;
    @FXML private TableColumn<ElementoVenta, Double> colPrecio;

    @FXML private ComboBox<Promocion> comboPromociones;
    @FXML private Spinner<Integer> spinnerCantidad;
    @FXML private ListView<String> listaCarrito;
    @FXML private Label lblTotal;

    // Controles para Decorator
    @FXML private CheckBox chkMantequilla;
    @FXML private CheckBox chkQueso;

    private Cliente clienteActual;

    private final List<ElementoVenta> carrito = new ArrayList<>();

    public void configurarCliente(Cliente cliente) {
        this.clienteActual = cliente;
    }

    @FXML
    public void initialize() {

        colNombre.setCellValueFactory(datos ->
                new ReadOnlyStringWrapper(
                        datos.getValue().getNombre()
                )
        );

        colPrecio.setCellValueFactory(datos ->
                new ReadOnlyObjectWrapper<>(
                        datos.getValue().calcularPrecio()
                )
        );

        colPrecio.setCellFactory(columna ->
                new TableCell<>() {

                    @Override
                    protected void updateItem(Double precio, boolean vacio) {
                        super.updateItem(precio, vacio);

                        setText(vacio || precio == null
                                ? null
                                : String.format("$%,.0f", precio));
                    }
                }
        );

        spinnerCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        1, 20, 1
                )
        );

        tablaProductos.getItems().setAll(
                Lanzador.gestorConfiteria.consultarCatalogo()
        );

        comboPromociones.getItems().setAll(
                Lanzador.gestorPromociones.consultarPromociones()
                        .stream()
                        .filter(Promocion::isActiva)
                        .toList()
        );

        comboPromociones.setPromptText("Sin promoción");

        comboPromociones.setCellFactory(lista -> new ListCell<>() {

            @Override
            protected void updateItem(Promocion promocion, boolean vacio) {
                super.updateItem(promocion, vacio);

                setText(vacio || promocion == null
                        ? null
                        : promocion.getNombre() + " ("
                        + promocion.getPorcentajeDescuento() + "%)");
            }
        });

        comboPromociones.setButtonCell(new ListCell<>() {

            @Override
            protected void updateItem(Promocion promocion, boolean vacio) {
                super.updateItem(promocion, vacio);

                setText(vacio || promocion == null
                        ? "Sin promoción"
                        : promocion.getNombre() + " ("
                        + promocion.getPorcentajeDescuento() + "%)");
            }
        });

        comboPromociones.setOnAction(evento -> actualizarCarrito());

        actualizarCarrito();
    }

    @FXML
    private void agregarAlCarrito() {

        ElementoVenta seleccionado =
                tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje("Selecciona un producto o combo.");
            return;
        }

        // Aplicamos Decorator sobre el elemento seleccionado.
        ElementoVenta personalizado = seleccionado;

        if (chkMantequilla.isSelected()) {
            personalizado = new DecoradorMantequilla(personalizado);
        }

        if (chkQueso.isSelected()) {
            personalizado = new DecoradorQueso(personalizado);
        }

        int cantidad = spinnerCantidad.getValue();

        for (int i = 0; i < cantidad; i++) {
            carrito.add(personalizado);
        }

        // Limpiamos los complementos para el siguiente producto.
        chkMantequilla.setSelected(false);
        chkQueso.setSelected(false);

        actualizarCarrito();
    }

    private void actualizarCarrito() {

        listaCarrito.getItems().clear();

        double subtotal = 0;

        for (ElementoVenta elemento : carrito) {

            listaCarrito.getItems().add(
                    elemento.getNombre()
                            + " - $"
                            + String.format(
                            "%,.0f",
                            elemento.calcularPrecio()
                    )
            );

            subtotal += elemento.calcularPrecio();
        }

        // Strategy calcula el descuento final.
        double total = subtotal;

        Promocion promocion = comboPromociones.getValue();

        if (promocion != null && promocion.isActiva()) {

            EstrategiaDescuento estrategia =
                    new DescuentoPorcentaje(
                            promocion.getPorcentajeDescuento()
                    );

            total = estrategia.calcularTotal(subtotal);
        }

        lblTotal.setText(String.format("$%,.0f", total));
    }

    @FXML
    private void vaciarCarrito() {

        carrito.clear();
        actualizarCarrito();
    }

    @FXML
    private void confirmarCompra() {

        if (clienteActual == null) {
            mostrarMensaje("Debes iniciar sesión como cliente.");
            return;
        }

        if (carrito.isEmpty()) {
            mostrarMensaje("El carrito está vacío.");
            return;
        }

        var tarjeta = clienteActual.getTarjetaVirtual();

        if (tarjeta == null || !tarjeta.isActiva()) {
            mostrarMensaje("Tu tarjeta virtual está inactiva.");
            return;
        }

        try {

            CompraBuilder builder = new CompraBuilder()
                    .id(GeneradorConsecutivos.getInstancia().generarIdCompra())
                    .cliente(clienteActual)
                    .fecha(LocalDateTime.now());

            for (ElementoVenta elemento : carrito) {
                builder.agregarElemento(elemento);
            }

            Compra compra = builder.build();

            Promocion promocionSeleccionada =
                    comboPromociones.getValue();

            if (promocionSeleccionada != null
                    && promocionSeleccionada.isActiva()) {

                compra.aplicarPromocion(promocionSeleccionada);
            }

            double totalFinal = compra.getTotal();

            if (!Double.isFinite(totalFinal) || totalFinal <= 0) {
                mostrarMensaje(
                        "El total a pagar debe ser mayor que cero."
                );
                return;
            }

            if (tarjeta.getSaldo() < totalFinal) {

                mostrarMensaje(
                        "Saldo insuficiente.\n"
                                + "Subtotal: $"
                                + String.format(
                                "%,.0f",
                                compra.getSubtotal()
                        )
                                + "\nDescuento: $"
                                + String.format(
                                "%,.0f",
                                compra.getValorDescuento()
                        )
                                + "\nTotal a pagar: $"
                                + String.format("%,.0f", totalFinal)
                                + "\nSaldo disponible: $"
                                + String.format(
                                "%,.0f",
                                tarjeta.getSaldo()
                        )
                );

                return;
            }

            String numeroTarjeta = tarjeta.getNumero();

            boolean exitosa =
                    Lanzador.compraFacade.realizarCompraConfiteria(
                            clienteActual,
                            compra,
                            numeroTarjeta
                    );

            if (exitosa) {

                mostrarMensaje(
                        "¡Compra realizada correctamente!\n\n"
                                + "Subtotal: $"
                                + String.format(
                                "%,.0f",
                                compra.getSubtotal()
                        )
                                + "\nPromoción: "
                                + compra.getNombrePromocion()
                                + "\nDescuento aplicado: $"
                                + String.format(
                                "%,.0f",
                                compra.getValorDescuento()
                        )
                                + "\nTotal pagado: $"
                                + String.format(
                                "%,.0f",
                                compra.getTotal()
                        )
                                + "\nSaldo restante: $"
                                + String.format(
                                "%,.0f",
                                tarjeta.getSaldo()
                        )
                                + "\nPuntos disponibles: "
                                + clienteActual.getCuentaPuntos()
                                .consultarSaldo()
                );

                carrito.clear();
                comboPromociones.getSelectionModel().clearSelection();
                actualizarCarrito();

            } else {

                mostrarMensaje(
                        "No fue posible completar la compra.\n"
                                + "Verifica el saldo, el estado de la tarjeta "
                                + "y la autorización del pago."
                );
            }

        } catch (RuntimeException e) {

            mostrarMensaje(
                    "Error al procesar la compra: " + e.getMessage()
            );
        }
    }

    @FXML
    private void volver() {

        Stage ventana = (Stage) lblTotal
                .getScene()
                .getWindow();

        ventana.close();
    }

    private void mostrarMensaje(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("CinemaUQ");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
