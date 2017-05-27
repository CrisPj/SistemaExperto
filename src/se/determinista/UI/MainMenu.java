package se.determinista.UI;

import se.determinista.files.ArchivoHechos;
import se.determinista.files.ArchivoMaestro;
import se.determinista.inference.InferenceEngine;

import java.util.Scanner;

public class MainMenu {

    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    private InferenceEngine motorInferencia;

    public MainMenu() {
        String nombreArchivo = "baseConocimiento";
        archivoMaestro = new ArchivoMaestro(nombreArchivo, "rw");
        archivoHechos = new ArchivoHechos(nombreArchivo, "rw");
        motorInferencia = new InferenceEngine(archivoMaestro, archivoHechos);
        mostrarMenu();
    }

    public void mostrarMenu() {
        String opcionUsuario;
        do {
            System.out.println("Ingrese una de las siguientes opciones:\n" +
                    "1.- Ver las reglas en la base de conocimientos\n" +
                    "2.- Ver índice de reglas\n" +
                    "3.- Agregar reglas a la base de conocimientos\n" +
                    "4.- Borrar todas reglas a la base de conocimientos\n" +
                    "5.- Ver los antecedentes en la base de hechos\n" +
                    "6.- Agregar hechos a la base de hechos\n" +
                    "7.- Realizar inferencia (encadenamiento hacia adelante)\n" +
                    "8.- Realizar inferencia (encadenamiento hacia atrás)\n" +
                    "9.- Borrar todos los hechos\n" +
                    "0.- Salir");
            opcionUsuario = new Scanner(System.in).next();
            if (!opcionUsuario.equals("0")) {
                ejecutarOpcion(opcionUsuario);
            }
        } while (!opcionUsuario.equals("0"));
    }

    public void ejecutarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                archivoMaestro.imprimirReglas();
                break;
            case "2":
                archivoMaestro.mostrarIndex();
                break;
            case "3":
                archivoMaestro.insertarNuevasReglas();
                break;
            case "4":
                archivoMaestro.eliminarReglas();
                break;
            case "5":
                archivoHechos.imprimirHechos();
                break;
            case "6":
                archivoHechos.insertarHechos();
                break;
            case "7":
                motorInferencia.initialize();
                break;
            case "8":
                //motorInferencia.initialize();
                break;
            case "9":
                archivoHechos.borrarHechos();
                break;
        }
    }

}