package determinista;

import determinista.arbol.Indice;
import determinista.arbol.Regla;
import determinista.arbol.ReglaHackeada;
import determinista.archivos.ArchivoHechos;
import determinista.archivos.ArchivoMaestro;
import determinista.common.Constantes;
import determinista.inferencia.motorInferencia;
import io.vertx.core.json.Json;
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

    public ArrayList<Integer> hacerEncadenamientoAdelante(JsonObject meta)
    {
        motorInferencia mt = new motorInferencia(archivoMaestro,archivoHechos);
        mt.inicializar(true,meta.getString("meta"));
        return mt.justificacion();
    }

    public ArrayList<Integer> hacerEncadenamientoAtras(JsonObject meta)
    {
        motorInferencia mt = new motorInferencia(archivoMaestro,archivoHechos);
        mt.inicializar(false,meta.getString("meta"));
        return mt.justificacion();
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

    public boolean rmRegla(JsonObject json)
    {
        archivoMaestro.eliminarRegla(json.getInteger("llave"));
        return true;
    }

    public boolean rmReglas()
    {
        archivoMaestro.eliminarReglas();
        return true;
    }

    public boolean addRegla(JsonObject regla) {
        try {
            archivoMaestro.nuevoRegistro(mostrarRegla(regla.getString("regla")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Regla mostrarRegla(String entrada) throws Exception {
        int llave = archivoMaestro.imprimirReglas().get(archivoMaestro.imprimirReglas().size()-1).getLlave()+1;
        Regla r = new Regla(llave, entrada.split("-")[0].split("\\&"), entrada.split("-")[1]);;
        return r;
    }

    public boolean rmHecho(JsonObject hecho) {
        archivoHechos.borrarHecho(hecho.getString("hecho"));
        return true;
    }

    public boolean rmHechos(){
        archivoHechos.borrarHechos();
        return true;
    }
}
