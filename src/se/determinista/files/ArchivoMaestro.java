package se.determinista.files;

import se.determinista.tree.Arbol;
import se.determinista.tree.Regla;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class ArchivoMaestro
{
    public static String EXTENSION = ".master";
    private RandomAccessFile archivo;
    private ArchivoIndice index;
    private se.determinista.tree.Arbol Arbol;
    private String ruta;


    public ArchivoMaestro(String nombre, String permisos) {
        crearArchivo(nombre, permisos);
    }


    private void crearArchivo(String nombre, String permisos) {
        try {
            ruta = nombre;
            archivo = new RandomAccessFile(nombre + EXTENSION, permisos);
            index = new ArchivoIndice(nombre + ArchivoIndice.EXTENSION, permisos);
        } catch (Exception ex) {
            System.out.println("Fallo al crear archivo maestro");
        }
    }

    private void nuevoRegistro(Regla regla) {
        StringBuffer buffer;
        try {
            archivo.seek(archivo.length());
            index.nuevoRegistro(regla.getId(), archivo.getFilePointer());
            archivo.writeByte(regla.getId());
            for (int i = 0; i < Regla.RECORDS_QUANTITY; i++) {
                try {
                    buffer = new StringBuffer(regla.getRecords()[i]);
                } catch (Exception ex) {
                    buffer = new StringBuffer();
                }
                buffer.setLength(Regla.SINGULAR_RECORD_SIZE);
                archivo.writeChars(buffer.toString());
            }
            buffer = new StringBuffer(regla.getConsequent());
            buffer.setLength(Regla.SINGULAR_RECORD_SIZE);
            archivo.writeChars(buffer.toString());
        } catch (Exception ex) {
            System.out.println("Fallo al escribir en archivo maestro");
        }
    }

    public Regla obtenerRegla(byte numeroRegla) {
        Regla regla = new Regla();
        String[] registros = new String[Regla.RECORDS_QUANTITY];
        char[] registroActual = new char[Regla.SINGULAR_RECORD_SIZE];

        try {
            if (numeroRegla > 0) {
                archivo.seek(Arbol.getRuleMemoryAddress(numeroRegla));
                regla.setId(archivo.readByte());
                for (int recordNumber = 0; recordNumber < Regla.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Regla.SINGULAR_RECORD_SIZE; i++) {
                        registroActual[i] = archivo.readChar();
                    }
                    registros[recordNumber] = new String(registroActual);
                }
                regla.setRecords(registros);

                for (int i = 0; i < Regla.SINGULAR_RECORD_SIZE; i++) {
                    registroActual[i] = archivo.readChar();
                }
                regla.setConsequent(new String(registroActual));
            }
        } catch (Exception ex) {
            System.out.println("Error, la regla no existe en la base de conocimientos : " + ex.getMessage());
        }
        return regla;
    }

    public void insertarNuevasReglas() {
        if (new java.io.File(ruta + ArchivoMaestro.EXTENSION).exists()) {
            String input;
            do {
                System.out.println("Ingrese una nueva regla con el formato ID-Ant1^Ant2^...^Ant5-Consecuente \n O  \"x\" para salir");
                input = new Scanner(System.in).next();
                if (!input.equals("x"))
                    try {
                        nuevoRegistro(mostrarRegla(input));
                    } catch (Exception ex) {
                        System.out.println("Regla mal formada");
                    }
            } while (!input.equals("x"));
        } else {
            crearArchivo("baseConocimiento", "rw");
            insertarNuevasReglas();
        }
    }

    public void imprimirReglas() {
        byte ruleId;
        String[] recordsArray = new String[Regla.RECORDS_QUANTITY];
        char[] currCharacteristic = new char[Regla.SINGULAR_RECORD_SIZE];
         try {
            archivo.seek(0);
            do {
                ruleId = archivo.readByte();
                for (int recordNumber = 0; recordNumber < Regla.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Regla.SINGULAR_RECORD_SIZE; i++) {
                        currCharacteristic[i] = archivo.readChar();
                    }
                    recordsArray[recordNumber] = new String(currCharacteristic);
                }
                for (int i = 0; i < Regla.SINGULAR_RECORD_SIZE; i++) {
                    currCharacteristic[i] = archivo.readChar();
                }
                System.out.println("ID: " + ruleId + " " + obtenerRegistros(recordsArray) + "-> " + new String(currCharacteristic));
            } while (true);
        } catch (Exception ex) {
            System.out.println("\nFinished reading ArchivoMaestro\n");
        }
    }

    public void mostrarIndex() {
        index.mostrarIndice();
    }

    public String obtenerRegistros(String[] registros) {
        int counter = 0;
        String records = "";
        for (String registro : registros) {
            if (counter < Regla.RECORDS_QUANTITY) {
                if (!registro.trim().isEmpty()) {
                    records += registro + "^";
                }
            }
        }
        return records.substring(0, records.length() - 1);
    }

    public void generarArbol() {
        Arbol = new Arbol();
        Arbol.generarArbol();
    }

    private Regla mostrarRegla(String entrada) throws Exception {
        return new Regla(Byte.parseByte(entrada.split("-")[0]), entrada.split("-")[1].split("\\^"), entrada.split("-")[2]);
    }

    public ArrayList<Regla> mostrarTodasReglas() {
        ArrayList<Regla> reglas = new ArrayList<>();
        String[] registros;
        char[] registroActual;
        try {
            archivo.seek(0);
            Regla regla;
            do {
                regla = new Regla();
                registros = new String[Regla.RECORDS_QUANTITY];
                registroActual = new char[Regla.SINGULAR_RECORD_SIZE];
                regla.setId(archivo.readByte());
                for (int recordNumber = 0; recordNumber < Regla.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Regla.SINGULAR_RECORD_SIZE; i++) {
                        registroActual[i] = archivo.readChar();
                    }
                    registros[recordNumber] = new String(registroActual);
                }
                regla.setRecords(registros);
                archivo.skipBytes(Regla.SINGULAR_RECORD_SIZE * 2);
                reglas.add(regla);
            } while (true);
        } catch (Exception ex) {
            System.out.println("Todas las reglas leidas");
        }
        return reglas;
    }


    public void eliminarReglas() {
        try {
            archivo.setLength(0);
            index.limpiarArchivo();
        } catch (Exception ex) {
            System.out.println("Archivo no pudo ser eliminado");
        }
    }
}
