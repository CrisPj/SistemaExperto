package determinista;

import determinista.arbol.Indice;
import determinista.arbol.Regla;
import determinista.arbol.ReglaHackeada;
import determinista.archivos.ArchivoHechos;
import determinista.archivos.ArchivoMaestro;
import determinista.common.Constantes;
import io.vertx.core.json.JsonObject;

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

    List<ReglaHackeada> getAllRules()
    {
     ArrayList<Regla> reglitas = archivoMaestro.imprimirReglas();
     List<ReglaHackeada> retorno = new ArrayList<>();
        for (Regla regls : reglitas) {
            StringBuilder a= new StringBuilder();
            for (String reg :regls.getReglas())
            {

                a.append(reg).append("&");
                if (reg.equals(""))
                {
                    break;
                }

            }
            retorno.add(new ReglaHackeada(regls.getLlave(),a.toString().substring(0,a.toString().length()-2),regls.getConsecuente()));
        }
        return retorno;

    }

    public void addHecho(JsonObject hecho)
    {

        archivoHechos.insertarHecho(hecho.getString("hecho"));
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

    public boolean rmHecho(JsonObject hecho) {
        archivoHechos.borrarHecho(hecho.getString("hecho"));
        return true;
    }
}
