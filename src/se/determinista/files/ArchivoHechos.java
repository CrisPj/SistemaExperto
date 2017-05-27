package se.determinista.files;

import se.determinista.tree.Regla;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class ArchivoHechos
{
    private String EXTENSION = ".fct";
    private RandomAccessFile file;

    public ArchivoHechos(String _name, String _permissions) {
        try {
            file = new RandomAccessFile(_name + EXTENSION, _permissions);
        } catch (Exception ex) {
            System.out.println("Archivo no pudo ser creado");
        }
    }

    public void insertarHecho(String hecho) {
        try {
            StringBuilder buffer = new StringBuilder(hecho);
            buffer.setLength(Regla.SINGULAR_RECORD_SIZE);
            file.writeChars(buffer.toString());
        } catch (Exception ex) {
            System.out.println("No se pudo insertar hecho");
        }
    }

    public ArrayList<String> obtenerHechos() {
        ArrayList<String> hechos = new ArrayList<>();
        try {
            file.seek(0);
            do {
                char[] hecho = new char[Regla.SINGULAR_RECORD_SIZE];
                for (int i = 0; i < Regla.SINGULAR_RECORD_SIZE; i++)
                    hecho[i] = file.readChar();
                hechos.add(new String(hecho).trim());
            } while (true);
        } catch (Exception ex) {
            // System.out.println("Reached EOF");
        }
        if (hechos.size() == 0) hechos = null;
        return hechos;
    }

    public void imprimirHechos() {
        try {
            ArrayList<String> hechos = obtenerHechos();
            hechos.forEach(System.out::println);
        } catch (Exception ex) {
            System.out.println("\nFin de los hechos\n");
        }
    }

    public void insertarHechos() {
        String entrada;
        do {
            System.out.println("Ingrese un hecho a la base o FIN para finalizar");
            entrada = new Scanner(System.in).next();
            if (!entrada.equals("FIN")) {
                insertarHecho(entrada);
            }
        } while (!entrada.equals("FIN"));
    }

    public void borrarHechos() {
        try {
            file.setLength(0);
        } catch (Exception ex) {
            System.out.println("Hechos no pudieron ser eliminados");
        }
    }

}
