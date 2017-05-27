
package se.determinista.archivos;

import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author PythonTeam :v
 */
public class ArchivoIndice
{

    public final static String EXTENSION = ".index";
    private RandomAccessFile archivo;

    /**
     * @param nombre Nombre del archivo
     * @param permisos Permisos que tendra el archivo
     */
    public ArchivoIndice(String nombre, String permisos) {
        try {
            archivo = new RandomAccessFile(nombre, permisos);
        } catch (Exception ex) {
            System.out.println("Fallo al crear el archivo indice\n"+ ex.getMessage());
        }
    }

    /**
     *
     * Escribe un nuevo registro, especificando
     *
     * @param llave del registro
     * @param dirLogica del registro
     */
    public void nuevoRegistro(byte llave, long dirLogica) {
        try {
            archivo.seek(archivo.length());
            archivo.writeByte(llave);
            archivo.writeLong(dirLogica);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
    }

    /**
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getDirRegistros() {
        ArrayList<String> reglas = new ArrayList<>();
        try {
            archivo.seek(0);
            do {
                reglas.add(archivo.readByte() + "-" + archivo.readLong());
            } while (true);
        } catch (Exception ex) {
            System.out.println("Fin del índice: " + reglas.size());
        }
        return reglas;
    }

    /**
     * Shows in terminal the Index for the rules contained in the ArchivoMaestro
     */
    public void mostrarIndice() {
        try {
            archivo.seek(0);
            do {
                System.out.println("ID: " + archivo.readByte() + "-->" + archivo.readLong());
            } while (true);
        } catch (Exception ex) {
            System.out.println("\nTermine de leer ArchivoIndice\n");
        }
    }

    public void limpiarArchivo() {
        try {
            archivo.setLength(0);
        } catch (Exception ex) {
            System.out.println("Archivo no puede ser limpiado");
        }
    }
}
