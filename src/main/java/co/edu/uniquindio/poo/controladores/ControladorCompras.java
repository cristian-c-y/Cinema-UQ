package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.modelo.Cliente;
import co.edu.uniquindio.poo.modelo.Compra;
import co.edu.uniquindio.poo.modelo.DetalleCompra;
import co.edu.uniquindio.poo.patrones.estructurales.ElementoVenta;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class ControladorCompras {

    @FXML private TableView<Compra> tablaCompras;

    @FXML private TableColumn<Compra, String> colId;
    @FXML private TableColumn<Compra, String> colFecha;
    @FXML private TableColumn<Compra, Double> colTotal;

    @FXML private ListView<String> listaDetalles;

    private Cliente clienteActual;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void configurarCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarCompras();
    }

    @FXML
    private void initialize() {

        colId.setCellValueFactory(datos ->
                new ReadOnlyStringWrapper(
                        datos.getValue().getId()
                )
        );

        colFecha.setCellValueFactory(datos ->
                new ReadOnlyStringWrapper(
                        datos.getValue().getFecha().format(formatoFecha)
                )
        );

        colTotal.setCellValueFactory(datos ->
                new ReadOnlyObjectWrapper<>(
                        datos.getValue().getTotal()
                )
        );

        colTotal.setCellFactory(columna -> new TableCell<>() {

            @Override
            protected void updateItem(Double total, boolean vacio) {
                super.updateItem(total, vacio);

                setText(vacio || total == null
                        ? null
                        : String.format("$%,.0f", total));
            }
        });

        tablaCompras.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, seleccionada) ->
                        mostrarDetalle(seleccionada)
                );
    }

    @FXML
    private void actualizarCompras() {

        if (clienteActual == null) {
            return;
        }

        tablaCompras.getItems().setAll(
                clienteActual.getCompras()
        );

        listaDetalles.getItems().clear();
    }

    private void mostrarDetalle(Compra compra) {

        listaDetalles.getItems().clear();

        if (compra == null) {
            return;
        }

        listaDetalles.getItems().add(
                "COMPRA: " + compra.getId()
        );

        listaDetalles.getItems().add(
                "Fecha: " + compra.getFecha().format(formatoFecha)
        );

        listaDetalles.getItems().add("-----------------------------");

        listaDetalles.getItems().add("PRODUCTOS:");

        for (DetalleCompra detalle : compra.getDetalles()) {

            listaDetalles.getItems().add(
                    detalle.getNombre()
                            + " - $"
                            + String.format(
                            "%,.0f",
                            detalle.getPrecioUnitario()
                    )
            );
        }


        listaDetalles.getItems().add("-----------------------------");

        listaDetalles.getItems().add(
                "Subtotal: $"
                        + String.format("%,.0f", compra.getSubtotal())
        );

        listaDetalles.getItems().add(
                "Promoción: " + compra.getNombrePromocion()
        );

        listaDetalles.getItems().add(
                "Descuento: -$"
                        + String.format(
                        "%,.0f",
                        compra.getValorDescuento()
                )
        );

        listaDetalles.getItems().add(
                "TOTAL PAGADO: $"
                        + String.format("%,.0f", compra.getTotal())
        );
    }

    @FXML
    private void volver() {

        Stage ventana = (Stage) tablaCompras
                .getScene()
                .getWindow();

        ventana.close();
    }
}
