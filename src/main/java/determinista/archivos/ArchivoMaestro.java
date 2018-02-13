package determinista.archivos;

import determinista.arbol.Arbol;
import determinista.arbol.Indice;
import determinista.arbol.Regla;
import determinista.common.Constantes;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArchivoMaestro
{

    private RandomAccessFile archivo;
    private ArchivoIndice index;
    private Arbol Arbol;
    private String ruta;
    private List<Regla> reglas;


    public ArchivoMaestro(String nombre, String permisos)
    {
        reglas = new ArrayList<>();
        crearArchivo(nombre, permisos);
    }


    public void crearArchivo(String nombre, String permisos)
    {
        try {
            ruta = nombre;
            archivo = new RandomAccessFile(nombre + Constantes.EXTENCION_CONOCIMIENTO, permisos);
            index = new ArchivoIndice(nombre + Constantes.EXTENCION_INDICE, permisos);
        } catch (Exception ex) {
            System.out.println("Fallo al crear archivo maestro");
        }
    }

    public void nuevoRegistro(Regla regla) {
        reglas.add(regla);
        StringBuffer buffer;
        try {
            archivo.seek(archivo.length());
            index.nuevo(regla.getLlave(), archivo.getFilePointer());
            archivo.writeByte(regla.getLlave());
            for (int i = 0; i < Regla.CANTIDAD_REGISTROS; i++) {
                try {
                    buffer = new StringBuffer(regla.getReglas()[i]);
                } catch (Exception ex) {
                    buffer = new StringBuffer();
                }
                buffer.setLength(Regla.TAM_REGISTRO);
                archivo.writeChars(buffer.toString());
            }
            buffer = new StringBuffer(regla.getConsecuente());
            buffer.setLength(Regla.TAM_REGISTRO);
            archivo.writeChars(buffer.toString());
        } catch (Exception ex) {
            System.out.println("Fallo al escribir en archivo maestro");
        }
    }

    public Regla obtenerRegla(Integer numeroRegla) {
        Regla regla = new Regla();
        String[] registros = new String[Regla.CANTIDAD_REGISTROS];
        char[] registroActual = new char[Regla.TAM_REGISTRO];

        try {
            if (numeroRegla > 0) {
                archivo.seek(Arbol.getReglaDirLogica(numeroRegla));
                regla.setLlave(archivo.readByte());
                for (int recordNumber = 0; recordNumber < Regla.CANTIDAD_REGISTROS; recordNumber++) {
                    for (int i = 0; i < Regla.TAM_REGISTRO; i++) {
                        registroActual[i] = archivo.readChar();
                    }
                    registros[recordNumber] = new String(registroActual);
                }
                regla.setReglas(registros);

                for (int i = 0; i < Regla.TAM_REGISTRO; i++) {
                    registroActual[i] = archivo.readChar();
                }
                regla.setConsecuente(new String(registroActual));
            }
        } catch (Exception ex) {
            System.out.println("Error, la regla no existe en la base de conocimientos : " + ex.getMessage());
        }
        return regla;
    }

    public List<Regla> imprimirReglas() {
        byte ruleId;
        List<Regla> reglas = new ArrayList<>();
        String[] recordsArray = new String[Regla.CANTIDAD_REGISTROS];
        char[] currCharacteristic = new char[Regla.TAM_REGISTRO];
         try {
            archivo.seek(0);
            do {
                ruleId = archivo.readByte();
                for (int recordNumber = 0; recordNumber < Regla.CANTIDAD_REGISTROS; recordNumber++) {
                    for (int i = 0; i < Regla.TAM_REGISTRO; i++) {
                        currCharacteristic[i] = archivo.readChar();
                    }
                    recordsArray[recordNumber] = new String(currCharacteristic);
                }
                for (int i = 0; i < Regla.TAM_REGISTRO; i++) {
                    currCharacteristic[i] = archivo.readChar();
                }
                reglas.add(new Regla(ruleId,obtenerRegistrosList(recordsArray),new String(currCharacteristic).trim()));
            } while (true);
        } catch (Exception ex) {
            System.out.println("\nTermine de leer el ArchivoMaestro\n");
             return reglas;
        }
    }

    public List<Indice> mostrarIndex() {
        return index.mostrarIndice();
    }

    public String[] obtenerRegistrosList(String[] registros) {
        int counter = 0;
        List<String> records = new ArrayList<String>();
        for (String registro : registros) {
            if (counter < Regla.CANTIDAD_REGISTROS) {
                if (!registro.trim().isEmpty()) {
                    records.add(registro.trim());
                }
            }
        }
        return Arrays.copyOf(records.toArray(), records.size(), String[].class);
    }

    public void generarArbol() {
        Arbol = new Arbol();
        Arbol.generarArbol();
    }

    private Regla mostrarRegla(String entrada) throws Exception {
        return new Regla(Byte.parseByte(entrada.split("-")[0]), entrada.split("-")[1].split("\\&"), entrada.split("-")[2]);
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
                registros = new String[Regla.CANTIDAD_REGISTROS];
                registroActual = new char[Regla.TAM_REGISTRO];
                regla.setLlave(archivo.readByte());
                for (int recordNumber = 0; recordNumber < Regla.CANTIDAD_REGISTROS; recordNumber++) {
                    for (int i = 0; i < Regla.TAM_REGISTRO; i++) {
                        registroActual[i] = archivo.readChar();
                    }
                    registros[recordNumber] = new String(registroActual);
                }
                regla.setReglas(registros);
                archivo.skipBytes(Regla.TAM_REGISTRO * 2);
                reglas.add(regla);
            } while (true);
        } catch (Exception ex) {
            System.out.println("Todas las reglas leidas");
        }
        return reglas;
    }



    public void eliminarRegla(int llave) {
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
