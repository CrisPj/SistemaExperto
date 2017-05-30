package se.determinista.archivos;

import se.determinista.arbol.Regla;
import se.determinista.common.Constantes;

import java.io.RandomAccessFile;
import java.util.ArrayList;

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
        }
        if (hechos.size() == 0) hechos = null;
        return hechos;
    }

    public String imprimirHechos() {
        String retorno = "";
        for (String hecho : obtenerHechos())
            retorno += hecho + "\n";
        return retorno;
    }


    public void borrarHechos() {
        try {
            file.setLength(0);
        } catch (Exception ex) {
            System.out.println("Hechos no pudieron ser eliminados");
        }
    }

}
