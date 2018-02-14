package determinista;

import determinista.arbol.Indice;
import determinista.arbol.Regla;
import determinista.archivos.ArchivoHechos;
import determinista.archivos.ArchivoMaestro;
import determinista.common.Constantes;

import java.util.ArrayList;
import java.util.List;

public class API {
    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    String nombreArchivo = Constantes.NOMBRE_ARCHIVOS;

    API() {
        archivoMaestro = new ArchivoMaestro(nombreArchivo, Constantes.LECTURA_ESCRITURA);
        archivoHechos = new ArchivoHechos(nombreArchivo, Constantes.LECTURA_ESCRITURA);
        // Se borran los hechos al inicio del programa:
        archivoHechos.borrarHechos();
    }

    List<Indice> getIndex()
    {
        return archivoMaestro.mostrarIndex();
    }

    List<Regla> getAllRules()
    {
     return archivoMaestro.imprimirReglas();
    }

    public void addHecho(String hecho)
    {
        archivoHechos.insertarHecho(hecho);
    }

    public ArrayList<String> getAllHechos() {
        return archivoHechos.obtenerHechos();
    }

    public boolean rmRegla(int id)
    {
        return archivoMaestro.eliminarRegla(id);
    }

    public boolean addRegla(String regla) {
        try {
            archivoMaestro.nuevoRegistro(mostrarRegla(regla));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Regla mostrarRegla(String entrada) throws Exception {
        return new Regla(Byte.parseByte(entrada.split("-")[0]), entrada.split("-")[1].split("\\&"), entrada.split("-")[2]);
    }

    public void  rmHecho(String hecho) {
        archivoHechos.borrarHecho(hecho);
    }
}
