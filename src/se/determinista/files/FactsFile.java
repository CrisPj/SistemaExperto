package se.determinista.files;

import se.determinista.tree.Rule;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Andrés on 02/09/2016.
 */
public class FactsFile {

    private String FILE_EXTENSION = ".fct";

    private RandomAccessFile file;

    /**
     * Constructor of the class
     *
     * @param _name        Specify a name for the file
     * @param _permissions Which permissions access will have the file?
     */
    public FactsFile(String _name, String _permissions) {
        try {
            file = new RandomAccessFile(_name + FILE_EXTENSION, _permissions);
        } catch (Exception ex) {
        }
    }

    /**
     * This method writes a new fact in the file if the permissions are enabled
     *
     * @param _fact
     */
    public void insertFact(String _fact) {
        try {
            StringBuffer buffer = new StringBuffer(_fact);
            buffer.setLength(Rule.SINGULAR_RECORD_SIZE);
            file.writeChars(buffer.toString());
        } catch (Exception ex) {
        }
    }

    /**
     * Returns
     *
     * @return An ArrayList with all the facts
     */
    public ArrayList<String> getAllFacts() {
        ArrayList<String> facts = new ArrayList<>();
        try {
            file.seek(0);
            do {
                char[] fact = new char[Rule.SINGULAR_RECORD_SIZE];
                for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++)
                    fact[i] = file.readChar();
                facts.add(new String(fact).trim());
            } while (true);
        } catch (Exception ex) {
            // System.out.println("Reached EOF");
        }
        if (facts.size() == 0) facts = null;
        return facts;
    }

    public void printAllFacts() {
        try {
            ArrayList<String> facts = getAllFacts();
            for (String s : facts)
                System.out.println(s);
        } catch (Exception ex) {
            System.out.println("\nFin de los hechos\n");
        }
    }

    public void insertFacts() {
        String a = null;
        do {
            System.out.println("Ingrese un hecho a la base o \"x\" para finalizar");
            a = new Scanner(System.in).next();
            if (!a.equals("x")) {
                insertFact(a);
            }
        } while (!a.equals("x"));
    }

    public void deleteAllFacts() {
        try {
            file.setLength(0);
        } catch (Exception ex) {
        }
    }

}
