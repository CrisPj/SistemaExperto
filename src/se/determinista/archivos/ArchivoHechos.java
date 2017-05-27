package se.determinista.archivos;

import se.determinista.arbol.Regla;
import se.determinista.common.Constantes;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class ArchivoHechos
{

    private RandomAccessFile file;

    public ArchivoHechos(String nombre, String permisos) {
        try {
            file = new RandomAccessFile(nombre + Constantes.EXTENCION_HECHOS, permisos);
        } catch (Exception ex) {
            System.out.println("Archivo no pudo ser creado");
        }
    }

    public void insertarHecho(String hecho) {
        try {
            StringBuilder buffer = new StringBuilder(hecho);
            buffer.setLength(Regla.TAM_REGISTRO);
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
                char[] hecho = new char[Regla.TAM_REGISTRO];
                for (int i = 0; i < Regla.TAM_REGISTRO; i++)
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
