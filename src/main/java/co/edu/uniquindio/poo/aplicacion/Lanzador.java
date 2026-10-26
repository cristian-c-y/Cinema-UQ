package co.edu.uniquindio.poo.aplicacion;

import co.edu.uniquindio.poo.modelo.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import co.edu.uniquindio.poo.patrones.estructurales.CompraFacade;
import co.edu.uniquindio.poo.patrones.estructurales.PagoAdapter;
import co.edu.uniquindio.poo.patrones.estructurales.ServicioPagoExterno;


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

    public static final CompraFacade compraFacade =
            new CompraFacade(
                    gestorAsientos,
                    gestorPuntos,
                    new PagoAdapter(new ServicioPagoExterno())
            );

    @Override
    public void start(Stage escenario) throws IOException {

        // Usuarios de prueba
        gestorUsuarios.registrarUsuario(
                new Cliente(
                        "C001",
                        "Cliente de prueba",
                        "cliente@cinemauq.com",
                        "1234"
                )
        );

        gestorUsuarios.registrarUsuario(
                new Administrador(
                        "A001",
                        "Administrador",
                        "admin@cinemauq.com",
                        "admin123"
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
                LocalDateTime.now().plusDays(1).withHour(15).withMinute(0)
        );

        Funcion funcion2 = new Funcion(
                "F002",
                pelicula1,
                sala2,
                LocalDateTime.now().plusDays(1).withHour(19).withMinute(0)
        );

        Funcion funcion3 = new Funcion(
                "F003",
                pelicula2,
                sala3,
                LocalDateTime.now().plusDays(2).withHour(20).withMinute(0)
        );

        Funcion funcion4 = new Funcion(
                "F004",
                pelicula3,
                sala1,
                LocalDateTime.now().plusDays(2).withHour(17).withMinute(0)
        );

        gestorCartelera.agregarFuncion(funcion1);
        gestorCartelera.agregarFuncion(funcion2);
        gestorCartelera.agregarFuncion(funcion3);
        gestorCartelera.agregarFuncion(funcion4);


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

    public static void main(String[] args) {
        launch(args);
    }
}