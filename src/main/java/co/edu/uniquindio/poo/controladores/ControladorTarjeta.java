package co.edu.uniquindio.poo.controladores;

import co.edu.uniquindio.poo.modelo.Cliente;
import co.edu.uniquindio.poo.modelo.Movimiento;
import co.edu.uniquindio.poo.modelo.TarjetaVirtual;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ControladorTarjeta {

    @FXML private Label lblNumero;
    @FXML private Label lblSaldo;
    @FXML private Label lblEstado;
    @FXML private Label lblPuntos;

    @FXML private TableView<Movimiento> tablaMovimientos;
    @FXML private TableColumn<Movimiento, String> colTipo;
    @FXML private TableColumn<Movimiento, Double> colValor;
    @FXML private TableColumn<Movimiento, String> colDescripcion;

    private Cliente clienteActual;

    public void configurarCliente(Cliente cliente) {
        this.clienteActual = cliente;
        actualizarDatos();
    }

    @FXML
    private void initialize() {

        colTipo.setCellValueFactory(datos ->
                new ReadOnlyStringWrapper(
                        datos.getValue().getTipo()
                )
        );

        colValor.setCellValueFactory(datos ->
                new ReadOnlyObjectWrapper<>(
                        datos.getValue().getValor()
                )
        );

        colValor.setCellFactory(columna ->
                new TableCell<>() {

                    @Override
                    protected void updateItem(Double valor, boolean vacio) {
                        super.updateItem(valor, vacio);

                        setText(vacio || valor == null
                                ? null
                                : String.format("$%,.0f", valor));
                    }
                }
        );

        colDescripcion.setCellValueFactory(datos ->
                new ReadOnlyStringWrapper(
                        datos.getValue().getDescripcion()
                )
        );
    }

    @FXML
    private void actualizarDatos() {

        if (clienteActual == null) {
            return;
        }

        TarjetaVirtual tarjeta = clienteActual.getTarjetaVirtual();

        lblNumero.setText(tarjeta.getNumero());

        lblSaldo.setText(
                String.format("$%,.0f", tarjeta.getSaldo())
        );

        lblEstado.setText(
                tarjeta.isActiva() ? "ACTIVA" : "INACTIVA"
        );

        lblPuntos.setText(
                String.valueOf(
                        clienteActual.getCuentaPuntos().consultarSaldo()
                )
        );

        tablaMovimientos.getItems().setAll(
                tarjeta.getMovimientos()
        );
    }

    @FXML
    private void volver() {
        Stage ventana = (Stage) lblSaldo.getScene().getWindow();
        ventana.close();
    }
}
