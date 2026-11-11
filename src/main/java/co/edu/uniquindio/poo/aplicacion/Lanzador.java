package co.edu.uniquindio.poo.aplicacion;

import co.edu.uniquindio.poo.modelo.*;
import co.edu.uniquindio.poo.patrones.estructurales.CompraFacade;
import co.edu.uniquindio.poo.patrones.estructurales.PagoAdapter;
import co.edu.uniquindio.poo.patrones.estructurales.ServicioPagoExterno;
import co.edu.uniquindio.poo.patrones.creacionales.CreadorProducto;
import co.edu.uniquindio.poo.patrones.creacionales.CreadorCombo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class Lanzador extends Application {

    public static final GestorUsuarios gestorUsuarios =
            new GestorUsuarios();

    public static final GestorCartelera gestorCartelera =
            new GestorCartelera();

    public static final GestorAsientos gestorAsientos =
            new GestorAsientos();

    public static final GestorPuntos gestorPuntos =
            new GestorPuntos();

    public static final GestorConfiteria gestorConfiteria =
            new GestorConfiteria();

    public static final GestorPromociones gestorPromociones =
            new GestorPromociones();

    public static final CompraFacade compraFacade =
            new CompraFacade(
                    gestorAsientos,
                    gestorPuntos,
                    new PagoAdapter(new ServicioPagoExterno())
            );

    @Override
    public void start(Stage escenario) throws IOException {

        gestorUsuarios.registrarUsuario(
                new Cliente(
                        "C001",
                        "Cliente de prueba",
                        "cliente",
                        "123"
                )
        );

        gestorUsuarios.registrarUsuario(
                new Administrador(
                        "A001",
                        "Administrador",
                        "admin",
                        "admin"
                )
        );

        Pelicula pelicula1 = new Pelicula(
                "P001", "Interestelar", "Ciencia ficción", 169, "12+"
        );

        Pelicula pelicula2 = new Pelicula(
                "P002", "El Conjuro", "Terror", 112, "15+"
        );

        Pelicula pelicula3 = new Pelicula(
                "P003", "Spider-Man: Sin camino a casa",
                "Acción", 148, "12+"
        );

        gestorCartelera.agregarPelicula(pelicula1);
        gestorCartelera.agregarPelicula(pelicula2);
        gestorCartelera.agregarPelicula(pelicula3);

        Sala sala1 = new Sala("S001", "Sala 1");
        Sala sala2 = new Sala("S002", "Sala 2");
        Sala sala3 = new Sala("S003", "Sala 3");

        gestorCartelera.agregarSala(sala1);
        gestorCartelera.agregarSala(sala2);
        gestorCartelera.agregarSala(sala3);

        Sala[] salas = {sala1, sala2, sala3};

        for (Sala sala : salas) {

            for (char fila = 'A'; fila <= 'E'; fila++) {

                for (int numero = 1; numero <= 8; numero++) {

                    Asiento asiento = new Asiento(
                            String.valueOf(numero),
                            String.valueOf(fila)
                    );

                    sala.agregarAsiento(asiento);
                }
            }
        }

        Funcion funcion1 = new Funcion(
                "F001",
                pelicula1,
                sala1,
                LocalDateTime.now()
                        .plusDays(1)
                        .withHour(15)
                        .withMinute(0)
        );

        Funcion funcion2 = new Funcion(
                "F002",
                pelicula1,
                sala2,
                LocalDateTime.now()
                        .plusDays(1)
                        .withHour(19)
                        .withMinute(0)
        );

        Funcion funcion3 = new Funcion(
                "F003",
                pelicula2,
                sala3,
                LocalDateTime.now()
                        .plusDays(2)
                        .withHour(20)
                        .withMinute(0)
        );

        Funcion funcion4 = new Funcion(
                "F004",
                pelicula3,
                sala1,
                LocalDateTime.now()
                        .plusDays(2)
                        .withHour(17)
                        .withMinute(0)
        );

        gestorCartelera.agregarFuncion(funcion1);
        gestorCartelera.agregarFuncion(funcion2);
        gestorCartelera.agregarFuncion(funcion3);
        gestorCartelera.agregarFuncion(funcion4);

        cargarConfiteria();

        FXMLLoader cargador = new FXMLLoader(
                Lanzador.class.getResource("/vista/login.fxml")
        );

        Scene escena = new Scene(cargador.load(), 900, 600);

        escenario.setTitle("CinemaUQ - Iniciar sesión");
        escenario.setScene(escena);
        escenario.setMinWidth(750);
        escenario.setMinHeight(500);
        escenario.show();
    }

    private static void cargarConfiteria() {

        Producto crispetas = (Producto) new CreadorProducto(
                "PR001", "Crispetas grandes", 12000
        ).crearElemento();

        Producto gaseosa = (Producto) new CreadorProducto(
                "PR002", "Gaseosa grande", 7000
        ).crearElemento();

        Producto nachos = (Producto) new CreadorProducto(
                "PR003", "Nachos con queso", 10000
        ).crearElemento();

        Producto perro = (Producto) new CreadorProducto(
                "PR004", "Perro caliente", 9000
        ).crearElemento();


        Combo comboPersonal = (Combo) new CreadorCombo(
                "CB001", "Combo personal"
        ).crearElemento();

        comboPersonal.agregarElemento(crispetas);
        comboPersonal.agregarElemento(gaseosa);

        Combo comboFamiliar = (Combo) new CreadorCombo(
                "CB002", "Combo familiar"
        ).crearElemento();

        comboFamiliar.agregarElemento(crispetas);
        comboFamiliar.agregarElemento(gaseosa);
        comboFamiliar.agregarElemento(nachos);
        comboFamiliar.agregarElemento(perro);

        gestorConfiteria.agregarElemento(crispetas);
        gestorConfiteria.agregarElemento(gaseosa);
        gestorConfiteria.agregarElemento(nachos);
        gestorConfiteria.agregarElemento(perro);

        gestorConfiteria.agregarElemento(comboPersonal);
        gestorConfiteria.agregarElemento(comboFamiliar);
    }

    public static void main(String[] args) {
        launch(args);
    }
}