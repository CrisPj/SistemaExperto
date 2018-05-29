package determinista;

import determinista.archivos.ArchivoHechos;
import determinista.archivos.ArchivoMaestro;
import determinista.common.Constantes;
import determinista.inferencia.motorInferencia;
import determinista.modelos.Indice;
import determinista.modelos.Regla;
import determinista.modelos.ReglaSimple;
import io.vertx.core.json.JsonObject;
import determinista.Neuronal.MultiLayerPerceptron;
import determinista.Neuronal.transferfunctions.SigmoidalTransfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class API {
    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    String nombreArchivo = Constantes.NOMBRE_ARCHIVOS;
    HashSet<String> entrada;
    HashSet<String> salida;
    MultiLayerPerceptron net;
    String[] x;

    API() {
        archivoMaestro = new ArchivoMaestro(nombreArchivo, Constantes.LECTURA_ESCRITURA);
        archivoHechos = new ArchivoHechos(nombreArchivo, Constantes.LECTURA_ESCRITURA);
        // Se borran los hechos al inicio del programa:
        archivoHechos.borrarHechos();
    }

    public ArrayList<Regla> hacerEncadenamientoAdelante(JsonObject meta)
    {
        motorInferencia mt = new motorInferencia(archivoMaestro,archivoHechos);
        mt.inicializar(true,meta.getString("meta"));
        return mt.justificacion();
    }

    public boolean hacerEncadenamientoAtras()
    {
        if (net==null)
            return false;
        else
        {
            ArrayList<String> xs = getAllHechos();
            if (xs.size() == 0)
                return false;

            double[] input = new double[entrada.size()];
            for (int i1 = 0; i1 < xs.size(); i1++) {
                String hecho = xs.get(i1);
                for (int i = 0; i < x.length; i++) {
                    if (x[i].equals(hecho)) {
                        input[i] = 1;
                    }
                }
            }
            double output[] = net.execute(input);
            double max = 0;
            int idH = 0;
            for (int i = 0; i < output.length; i++) {
                if (output[i] > max)
                {
                    max = output[i];
                    idH = i;
                }

            }
            archivoHechos.insertarHecho((String) salida.toArray()[idH]);
            return true;
        }
    }

    List<Indice> getIndex()
    {
        return archivoMaestro.mostrarIndex();
    }

    List<ReglaSimple> getAllRules()
    {
     ArrayList<Regla> reglitas = archivoMaestro.imprimirReglas();
     List<ReglaSimple> retorno = new ArrayList<>();
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
            retorno.add(new ReglaSimple(regls.getLlave(),a.toString().substring(0,a.toString().length()-2),regls.getConsecuente()));
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
        archivoMaestro.eliminarTodo();
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

    public Boolean entrenar() {
        entrada = new HashSet<String>();
        ArrayList<Regla> reglas = archivoMaestro.imprimirReglas();
        reglas.forEach(r -> IntStream.range(0, r.getReglas().length).filter(i -> !r.getReglas()[i].equals("")).mapToObj(i -> r.getReglas()[i]).forEach(entrada::add));
        salida = reglas.stream().map(Regla::getConsecuente).collect(Collectors.toCollection(HashSet::new));
        x = entrada.toArray(new String[0]);
        String[] y = salida.toArray(new String[0]);

        double input[][] = new double[reglas.size()][entrada.size()];
        double output[][] = new double[reglas.size()][salida.size()];

        for (int i1 = 0; i1 < reglas.size(); i1++) {
            Regla regla = reglas.get(i1);

                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < regla.getReglas().length; j++) {
                        if (regla.getReglas()[j].equals(x[i]))
                        {
                            input[i1][i] = 1;
                            break;
                        }
                    }
                }

            for (int j = 0; j < y.length; j++) {
                output[i1][j] = 0;
                if (regla.getConsecuente().equals(y[j])) {
                    output[i1][j] = 1;
                }
            }
        }

        int[] layers = new int[]{ entrada.size(), reglas.size(), salida.size() };
       net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransfer());
        for(int i = 0; i < 50000; i++)
        {
            for (int j = 0; j < input.length; j++) {
                double[] inputs = input[j];
                double[] outputs = output[j];
                double error = net.backPropagate(inputs, outputs);
            }
        }

        /*

        double[] inputs = new double[]{1.0, 0.0};
		double[] output = net.execute(inputs);
         */
        return true;
    }
}
