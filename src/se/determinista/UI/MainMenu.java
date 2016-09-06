package se.determinista.UI;

import se.determinista.files.FactsFile;
import se.determinista.files.MasterFile;
import se.determinista.inference.InferenceEngine;

import java.util.Scanner;

/**
 * Created by Andrés on 03/09/2016.
 */
public class MainMenu {

    private static String path = "E:\\knowledgebase";

    private MasterFile mfile;
    private FactsFile ffile;
    private InferenceEngine engine;

    /**
     * Creates a new Menu and the files of the system
     */
    public MainMenu() {
        mfile = new MasterFile(path, "rw");
        ffile = new FactsFile(path, "rw");
        engine = new InferenceEngine(mfile, ffile);
        showMenu();
    }

    /**
     * Shows the main Menu
     */
    public void showMenu() {
        String option = null;
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
                    "x.- Salir");
            option = new Scanner(System.in).next();
            if (!option.equals("x")) {
                manageOption(option);
            }
        } while (!option.equals("x"));
    }

    /**
     * Manages the chosen option of the user
     *
     * @param _option
     */
    public void manageOption(String _option) {
        switch (_option) {
            case "1":
                mfile.printAllRules();
                break;
            case "2":
                mfile.showIndex();
                break;
            case "3":
                mfile.insertNewRules();
                break;
            case "4":
                mfile.deleteAllRules();
                break;
            case "5":
                ffile.printAllFacts();
                break;
            case "6":
                ffile.insertFacts();
                break;
            case "7":
                engine.initialize();
                break;
            case "8":
                //engine.initialize();
                break;
            case "9":
                ffile.deleteAllFacts();
                break;
        }
    }

}